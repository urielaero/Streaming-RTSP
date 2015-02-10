package rtp;

/**
 *
 * @author candysansores
 */
public class RTPpacket{

  //size of the RTP header:
  static int HEADER_SIZE = 12;

  //Fields that compose the RTP header
  public int Version;
  public int Padding;
  public int Extension;
  public int CC;
  public int Marker;
  public int PayloadType;
  public int SequenceNumber;
  public int TimeStamp;
  public int Ssrc;
  
  //Bitstream of the RTP header
  public byte[] header;

  //size of the RTP payload
  public int payload_size;
  //Bitstream of the RTP payload
  public byte[] payload;
  


  //--------------------------
  //Constructor of an RTPpacket object from header fields and payload bitstream
  //--------------------------
  public RTPpacket(int PType, int Framenb, int Time, byte[] data, int data_length){
    //fill by default header fields:
    Version = 2;
    Padding = 0;
    Extension = 0;
    CC = 0;
    Marker = 0;
    Ssrc = 0;

    //fill changing header fields:
    SequenceNumber = Framenb;
    TimeStamp = Time;
    PayloadType = PType;
    
    //build the header bistream:
    //--------------------------
    header = new byte[HEADER_SIZE];

    //.............
    //TO COMPLETE
    //.............
    //fill the header array of byte with RTP header fields

    //header[0] = ...
    //byte v_p_e = (byte)(Version << 6); //todo lo demas es cero no aporta nada...    
    header[0] = (byte)(Version << 6); //todo lo demas es cero no aporta nada...
    //fill the payload bitstream:
    header[1] = (byte) ( (Marker << 7) | (PayloadType) );



    header[2] = (byte) ( SequenceNumber >> 8 ); // para quedarce con los bits mas significativos.
    header[3] = (byte) ( SequenceNumber & 0xFF ); //para quedarce con los bits menos significativos
    
    //timestap 24 b
    header[4] = (byte) ( TimeStamp >> 24 );
    header[5] = (byte) ( TimeStamp >> 16 );
    header[6] = (byte) ( TimeStamp >> 8 );
    header[7] = (byte) ( TimeStamp >> 0xFF );

    //SSRC
    header[8] = (byte) ( Ssrc >> 24 );
    header[9] = (byte) ( Ssrc >> 16 );
    header[10] = (byte) ( Ssrc >> 8 );
    header[11] = (byte) ( Ssrc & 0xFF );
    
    payload_size = data_length;
    payload = new byte[data_length];
    
    //fill payload array of byte from data (given in parameter of the constructor)
    payload = data;
    
    // ! Do not forget to uncomment method printheader() below !

  }
    
  //--------------------------
  //Constructor of an RTPpacket object from the packet bistream 
  //--------------------------
  public RTPpacket(byte[] packet, int packet_size)
  {
    //fill default fields:
    Version = 2;
    Padding = 0;
    Extension = 0;
    CC = 0;
    Marker = 0;
    Ssrc = 0;

    //check if total packet size is lower than the header size
    if (packet_size >= HEADER_SIZE) 
      {
	//get the header bitsream:
	header = new byte[HEADER_SIZE];
	System.out.println("PACKET COM");
	for (int i=0; i < HEADER_SIZE; i++){
	  System.out.println(packet[i]);
	  header[i] = packet[i];
	
	}
	System.out.println("PACKET TERM.....");

	//get the payload bitstream:
	payload_size = packet_size - HEADER_SIZE;
	payload = new byte[payload_size];
	for (int i=HEADER_SIZE; i < packet_size; i++)
	  payload[i-HEADER_SIZE] = packet[i];

	//interpret the changing fields of the header:
	PayloadType = header[1] & 127;
	SequenceNumber = unsigned_int(header[3]) + 256*unsigned_int(header[2]);
	TimeStamp = unsigned_int(header[7]) + 256*unsigned_int(header[6]) + 65536*unsigned_int(header[5]) + 16777216*unsigned_int(header[4]);
      }
 }

  //--------------------------
  //getpayload: return the payload bistream of the RTPpacket and its size
  //--------------------------
  public int getpayload(byte[] data) {
    System.out.println("payload[i]");
    for (int i=0; i < payload_size; i++){
      System.out.print(payload[i]);
      data[i] = payload[i];
    }
    System.out.println(" ");

    return(payload_size);
  }

  //--------------------------
  //getpayload_length: return the length of the payload
  //--------------------------
  public int getpayload_length() {
    return(payload_size);
  }

  //--------------------------
  //getlength: return the total length of the RTP packet
  //--------------------------
  public int getlength() {
    return(payload_size + HEADER_SIZE);
  }

  //--------------------------
  //getpacket: returns the packet bitstream and its length
  //--------------------------
  public int getpacket(byte[] packet)
  {
    //construct the packet = header + payload
    for (int i=0; i < HEADER_SIZE; i++)
	packet[i] = header[i];
    for (int i=0; i < payload_size; i++)
	packet[i+HEADER_SIZE] = payload[i];

    //return total size of the packet
    return(payload_size + HEADER_SIZE);
  }

  //--------------------------
  //gettimestamp
  //--------------------------

  public int gettimestamp() {
    return(TimeStamp);
  }

  //--------------------------
  //getsequencenumber
  //--------------------------
  public int getsequencenumber() {
    return(SequenceNumber);
  }

  //--------------------------
  //getpayloadtype
  //--------------------------
  public int getpayloadtype() {
    return(PayloadType);
  }


  //--------------------------
  //print headers without the SSRC
  //--------------------------
  public void printheaderdos()
  {
    //TO DO: uncomment
    /*
    for (int i=0; i < (HEADER_SIZE-4); i++)
      {
	for (int j = 7; j>=0 ; j--)
	  if (((1<= 0)
      return(nb);
    else
      return(256+nb);
      */
  }
  
  public void printheader()
	{
		for (int i=0; i < (HEADER_SIZE); i++)
		{
			for (int j = 7; j>=0 ; j--)
				if (((1<<j) & header[i] ) != 0)
					System.out.print("1");
				else
					System.out.print("0");
			System.out.print(" ");
		}

		System.out.println();

	}

  
  private int unsigned_int(int num){
	  if(num>=0)
		  return num;
	  return -1*num;
	  
	  
  }

}

