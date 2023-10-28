import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.BorderLayout;
import java.io.*;
import java.net.*;


import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
public class client extends JFrame
 {
    Socket socket;
    BufferedReader br;// for input in server from sockect , client
     PrintWriter out;
   private JLabel heading=new JLabel("Client");
    private JTextArea messageArea=new JTextArea();
    private JTextField message = new JTextField();
   private Font font=new Font("Roboto",Font.PLAIN,21);

     public client(){
        try{
       System.out.println("SEnding Request to SErver");
          socket = new Socket("192.168.72.143",7778);
           System.out.println("Connection Done");


           br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    //sockect s input stream nikali had given to inputstreamreader the data which is in byte , will get converted into char
        out = new PrintWriter(socket.getOutputStream());
        // out is for sending data to socket
                   
        createGUI();
        handleEvents();






   startReading();
       startWriting();
        }catch(Exception e){

        }
    }
    public void handleEvents(){
         

         message.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

           @Override
           public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
               
            }

        @Override
           public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                //System.out.println("Key released"+e.getKeyCode());
               if (e.getKeyCode() == 10 ){
                   String sent = message.getText();
                   messageArea.append("Client :"+sent+"\n");
                    out.println(sent);
                    out.flush();
                   message.setText(""); 
                    message.requestFocus();

             }
                
           }

        });
   }
   private void createGUI(){
        this.setTitle("Client Messanger");
       this.setSize(600,600);
       this.setLocationRelativeTo(null); //window will always be on centre
       this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        heading.setFont(font);
       messageArea.setFont(font);
     message.setFont(font);



      //heading ko centerm krne k liye 
     // heading.setIcon(new ImageIcon("image.png"));
      //heading.setHorizontalTextPosition(SwingConstants.BOTTOM);
     // heading.setHorizontalTextPosition(SwingConstants.CENTER);
       heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        heading.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        heading.setForeground(Color.BLACK);
        //frame ka layout set krenge 
       messageArea.setEditable(false);
       this.setLayout(new BorderLayout());

        //components for frame 
        this.add(heading,BorderLayout.NORTH);
          JScrollPane jScrollPane = new JScrollPane(messageArea);
      this.add(messageArea,BorderLayout.CENTER);
       this.add(message,BorderLayout.SOUTH);


       this.setVisible(true);
    }
    public void startReading(){
        //threadread krke deta rhega 
        Runnable r1 = ()->{
            System.out.println("Reader started");
            try {
            while(true)
            {
             
                 String msg = br.readLine();
                 if ( msg.equals("Exit")){
                     System.out.println("SERVER have terminated the chat");
                   JOptionPane.showMessageDialog(this,"Server Terminated");
                   message.setEnabled(false);
                     socket.close();
                     break;
                 }
           //  System.out.println("Server :"+msg);
                messageArea.append("Server :" +msg+"\n");
                 
             
            }
            } catch (IOException e) {
                 e.printStackTrace();  // Handle the exception
             } 
        };
        new Thread(r1).start();
     }
     public void startWriting(){
        // thread data user s lega and send krta rheg to clientt
        Runnable r2 = ()->{
         System.out.println("Writer started....");
         try {
         while(true && !socket.isClosed()){
                  BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                  String content = br1.readLine();
                 
                  out.println(content);
                  out.flush();//if the data isnot going to client it will forcefully transfer into client message box
                  // if (content.equals("Exit")){
                    //socket.close();
                    //break;
               //   }
            
         }
          }catch( Exception e){
                 e.printStackTrace();
             }
     
        }; 
         new Thread(r2).start();
     }
     
    public static void main (String args[]){
        System.out.println("Tis is client ");
        new client();
    }
}
