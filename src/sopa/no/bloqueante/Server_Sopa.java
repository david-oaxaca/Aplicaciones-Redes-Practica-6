package sopa.no.bloqueante;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author David Madrigal Buendía
 * @author David Arturo Oaxaca Pérez
 */
public class Server_Sopa {
    
    public static void main(String[] args) throws ClassNotFoundException {
        
        Operaciones_Sopa operaciones = new Operaciones_Sopa();
        
        int puerto = 4000;
        
        String host = "127.0.0.1";
        
        String [] Animales = {"PERRO", "GATO", "RATON", "AVESTRUZ", "PATO", "ORNITORRINCO", 
                                "MONO", "ZORRO", "LOBO", "MARIPOSA", "CABALLO", "CAMELLO", "JABALI",
                                "ZURICATA", "HURON", "TLACUACHE", "BALLENA", "TIBURON", "DINGO",
                                "CARPINCHO"};
        
        String [] Comida = {"PIZZA", "HAMBURGUESA", "SANDWICH", "SUSHI", "MILANESA", "TACOS",
                            "SPAGHETTI", "LASAGNA", "TLAYUDAS", "CEREAL", "BROWNIES", "GALLETAS",
                            "BOLILLO", "TORTA", "ENSALADA", "FILETE", "SOPA", "ALITAS", "NACHOS",
                            "ATUN"};
        
        String [] Paises = {"MEXICO", "CHILE", "BRAZIL", "FRANCIA", "ALEMANIA", "JAPON",
                            "ARGENTINA", "CHINA", "INDIA", "AUSTRALIA", "RUSIA", "ITALIA",
                            "IRLANDA", "SUECIA", "SUIZA", "HONDURAS", "BOLIVIA", "MADAGASCAR", 
                            "CANADA","POLONIA"};
        
        String [] Pokemon = {"CHARIZARD", "SQUIRTLE", "BULBASAUR", "BLAZIKEN", "MEWTWO", "SNORLAX",
                            "EEVE", "SYLVEON", "POOCHYENA", "METAPOD", "BIDOOF", "SCIZOR",
                            "LOTAD", "LUDICULO", "MEOWTH", "ARTICUNO", "ZAPDOS", "MOLTRES", 
                            "PIKACHU","ARCEUS"};
        
        
        
        try {
            
            ServerSocketChannel s = ServerSocketChannel.open();
            s.configureBlocking(false);
            s.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            s.socket().bind(new InetSocketAddress(puerto));
            Selector sel = Selector.open();
            s.register(sel,SelectionKey.OP_ACCEPT);
           System.out.println("Servicio iniciado..esperando clientes..");
            String Tema = "";
            
            while(true){
               sel.select();
               Iterator<SelectionKey>it =sel.selectedKeys().iterator();
               while(it.hasNext()){
                   SelectionKey k = (SelectionKey)it.next();
                   it.remove();
                   if(k.isAcceptable()){
                      SocketChannel cl = s.accept();
                      System.out.println("Cliente conectado desde->"+cl.socket().getInetAddress().getHostAddress()+":"+cl.socket().getPort());
                      cl.configureBlocking(false);
                      cl.register(sel,SelectionKey.OP_READ);
                      continue;
                  }//if
                  if(k.isWritable()){
                       SocketChannel ch = (SocketChannel)k.channel();
                       //ObjectOutputStream oos = new ObjectOutputStream(ch.socket().getOutputStream());
                       
                       char [][] sopa = new char[16][16];
                
                       
                       
                        if(Tema.contains("Animales")){
                            sopa = operaciones.llenarSopa(sopa, Animales);
                           
                        }else if(Tema.contains("Comida")){
                            sopa = operaciones.llenarSopa(sopa, Comida);
                           
                        }else if(Tema.contains("Paises")){
                            sopa = operaciones.llenarSopa(sopa, Paises);
                          
                        }else if(Tema.contains("Pokemon")){
                            sopa = operaciones.llenarSopa(sopa, Pokemon);
                        }
                    
                

                       
                        ArrayList <Coordenadas> palabras = operaciones.getPalabras();

                        System.out.println("\nPalabras y coordenadas de la sopa: \n");

                        for (int i = 0; i < palabras.size(); i++) {
                            System.out.print(palabras.get(i).getPalabra() + "  ");
                            System.out.print(palabras.get(i).getInicio_x()+ " , " + palabras.get(i).getInicio_y() + "  ");
                            System.out.print(palabras.get(i).getFinal_x() + " , " + palabras.get(i).getFinal_y());
                            System.out.println("");
                        }

                        Sopa sopa_envio = new Sopa(sopa, palabras);
                        
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        ObjectOutputStream out = null;
                        
                        try {
                            out = new ObjectOutputStream(bos);   
                            out.writeObject(sopa_envio);
                            out.flush();
                            byte[] yourBytes = bos.toByteArray();
                            ByteBuffer b = ByteBuffer.wrap(yourBytes);
                            ch.write(b);
                        } finally {
                            try {
                                bos.close();
                            } catch (IOException ex) {
                                Logger.getLogger(Server_Sopa.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        ch.close();
                       //k.interestOps(SelectionKey.OP_ACCEPT);
                       //else
                   } else if(k.isReadable()){
                       SocketChannel ch = (SocketChannel)k.channel();
                       ByteBuffer b = ByteBuffer.allocate(2000);
                       b.clear();
                       int n = ch.read(b);
                       b.flip();
                       Tema = new String(b.array());
                       
                       k.interestOps(SelectionKey.OP_WRITE);
                       continue;
                   }//if
               }//while
           }//while
            
        } catch (IOException ex) {
            Logger.getLogger(Server_Sopa.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
