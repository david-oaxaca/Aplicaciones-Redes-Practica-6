package sopa.no.bloqueante;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author David Madrigal Buendía
 * @author David Arturo Oaxaca Pérez
 */
public class Client_Sopa {
    private ArrayList <Coordenadas> palabras;//Palabras dentro de la sopa de letras
    private char [][] SopaDeLetras;
    private boolean seleccionar;
    private int[] punto1, punto2;
    private String palabra_encontrada;
    private int palabras_encontradas;
    private long tiempo_inicio;
    private int puerto = 4000;
    private String host = "127.0.0.1";
    
    public Client_Sopa(){
        seleccionar= false;
        punto1= new int[2];
        punto2= new int[2];
        palabra_encontrada= "";
        palabras_encontradas= 0;
    }
    
    public int guardarSeleccion(int x, int y) {
        if(!seleccionar) {
            seleccionar= true;
            punto1[0]= x;
            punto1[1]= y;
            return 1;
        }else {
            seleccionar= false;
            punto2[0]= x;
            punto2[1]= y;
            return verificarSeleccion();
        }
    }
    //Devuleve verdadero si la palabra fue correcta
    private int verificarSeleccion() {
        System.out.println("Puntos: " + punto1[0] + ", " + punto1[1] + ": " + punto2[0] + ", " + punto2[1]);
        for(Coordenadas palabra: palabras) {
            System.out.println("Puntos Palabra: " + palabra.getInicio_x() + ", " + palabra.getInicio_y() + ": " + palabra.getFinal_x() + ", " + palabra.getFinal_y());
            if(punto1[0] == palabra.getInicio_x() &&
                    punto1[1] == palabra.getInicio_y() &&
                    punto2[0] == palabra.getFinal_x() &&
                    punto2[1] == palabra.getFinal_y()) {
                System.out.println("Encontro: " + palabra.getPalabra());
                palabra_encontrada= palabra.getPalabra();
                palabras_encontradas++;
                return 2;
            }else if(punto1[0] == palabra.getFinal_x() &&
                        punto1[1] == palabra.getFinal_y() &&
                        punto2[0] == palabra.getInicio_x() &&
                        punto2[1] == palabra.getInicio_y()) {
                System.out.println("Encontro: " + palabra.getPalabra());
                palabra_encontrada= palabra.getPalabra();
                palabras_encontradas++;
                return 2;
            }
        }
        return 0;
    }
    
    public int getxPos1() { return punto1[0]; }
    public int getyPos1() { return punto1[1]; }
    public int getxPos2() { return punto2[0]; }
    public int getyPos2() { return punto2[1]; }
    public String getPalabraEncontrada() { return palabra_encontrada; }
    public int getPalabrasEncontradas() { return palabras_encontradas; }
    
    public void inicializarTiempo() {
        tiempo_inicio= System.currentTimeMillis();
    }
    
    public long terminarTiempo() {
        long tiempo_final= System.currentTimeMillis();
        return tiempo_final - tiempo_inicio;
    }
    
    public void requestSopa( String opc ) throws IOException, ClassNotFoundException {
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        SocketChannel cl = SocketChannel.open();
        cl.configureBlocking(false);
        Selector sel = Selector.open();
        cl.connect(new InetSocketAddress(host,puerto));
        cl.register(sel,SelectionKey.OP_CONNECT);
        boolean en_proceso = true;
           
        while(en_proceso){
            sel.select();
            Iterator<SelectionKey>it =sel.selectedKeys().iterator();
            while(it.hasNext()){
                SelectionKey k = (SelectionKey)it.next();
                it.remove();
                if(k.isConnectable()){
                    SocketChannel ch = (SocketChannel)k.channel();
                    if(ch.isConnectionPending()){
                        try{
                            ch.finishConnect();
                            System.out.println("Conexion establecida.. Escribe un mensaje <ENTER> para enviar \"SALIR\" para teminar");
                        }catch(Exception e){
                            e.printStackTrace();
                        }//catch
                    }//if_conectionpending
                    //ch.configureBlocking(false);
                    ch.register(sel, SelectionKey.OP_READ|SelectionKey.OP_WRITE);
                    continue;
                }//if
                if(k.isWritable()){
                    SocketChannel ch2 = (SocketChannel)k.channel();
                   ByteBuffer b = ByteBuffer.wrap(opc.getBytes());
                    ch2.write(b);
                     System.out.println("Mensaje enviado...");
                    k.interestOps(SelectionKey.OP_READ);
                    continue;
                } else if(k.isReadable()){
                    SocketChannel ch2 = (SocketChannel)k.channel();
                    ByteBuffer b = ByteBuffer.allocate(2000);
                    b.clear();
                    int n = ch2.read(b);
                    b.flip();
                    ByteArrayInputStream bis = new ByteArrayInputStream(b.array());
                    ObjectInput in = null;
                    try {
                        in = new ObjectInputStream(bis);
                            Sopa objeto = (Sopa)in.readObject();
                            this.SopaDeLetras = objeto.getSopa();
                            this.palabras = objeto.getPalabras();
                    } finally {
                        try {
                            if (in != null) {
                                in.close();
                            }
                        } catch (IOException ex) {
                         // ignore close exception
                        }
                    }

                    ch2.close();
                    en_proceso = false;
                    cl.close();
                    //k.interestOps(SelectionKey.OP_WRITE);
                    //continue;
                }//if
            }//while
        }//while
        
        
        
    }
    
    public ArrayList <String> getPalabrasB(){
        
        ArrayList <String> temp = new ArrayList<String>();
        
        for (Coordenadas palabra : this.palabras) {
            temp.add(palabra.getPalabra());
        }
        
        return temp;
    }
    
    public char [][] getSopaDeLetras(){
        
        return this.SopaDeLetras;
    }
}
