package opencv.usage;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.*;

import com.sun.org.apache.bcel.internal.generic.ReturnaddressType;



public class FaceClassifier extends Thread{
	//private static String imagename = "images.jpg";
	private static String abs_path = "/home/sasha/workspace/InteractiveVideo/bin/"; // dynamically change
	public Integer global_faces  = 0;
	private List<Rect> rectFaces;
	private List<BufferedImage> cropFaces;
	Mat processingImg;
	BufferedImage processingImgBuf;
	
	public FaceClassifier(BufferedImage bufferedImage, Integer facenum)
	{
		global_faces = facenum;
		byte[] pixels = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        CascadeClassifier testClass = new CascadeClassifier(
        		"res/haarcascades/haarcascade_frontalface_default.xml");
        reloadImg(bufferedImage);
	}
	
	public void run(){
		try {
			rectFaces = findFace(processingImg);
			Thread.yield();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); 
		}
				
	}
	
	public List<Rect> getRectFaces()
	{
		if(rectFaces == null)
		{
			rectFaces = new ArrayList<Rect>();
		}
		return rectFaces;
	}
	
	public FaceClassifier() {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        CascadeClassifier testClass = new CascadeClassifier(
        		"res/haarcascades/haarcascade_frontalface_default.xml");
		System.out.println("lol");
	}
	
	public FaceClassifier(BufferedImage bufferedImage){
		byte[] pixels = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        CascadeClassifier testClass = new CascadeClassifier(
        		"res/haarcascades/haarcascade_frontalface_default.xml");
        reloadImg(bufferedImage);
	}
	
	public List<Rect> findFace(Mat image) throws IOException{
    	
        System.out.println("\nRunning Main");
//        Highgui.imwrite("anywhere.png",image);
        
        CascadeClassifier Main = new CascadeClassifier();
        if(!Main.load("res/haarcascades/haarcascade_frontalface_default.xml"))
        		System.out.println("bad");
        
        MatOfRect eyeDetections = new MatOfRect(),faceDetections = new MatOfRect();
        MatOfRect mouthDetections = new MatOfRect();
        faceDetections = findAllFaces(Main,image);
         
        //global_faces+=faceDetections.toArray().length;
        
        int i = 0;
        
        cropFaces = new ArrayList<BufferedImage>();
        
        //processingImgBuf = mat2Img(image);
        
        for(Rect r: faceDetections.toArray())
        {
        	global_faces++;
        	 
        	 BufferedImage croppedTemp = processingImgBuf.getSubimage(r.x,r.y,r.width,r.height);
        	 ImageIO.write(croppedTemp, "jpg", new File("Snapshots/output"+global_faces+".jpg"));
             
        	 
        	 cropFaces.add(croppedTemp);
        }
        /*for(Rect r: faceDetections.toArray())
        {
        	Core.rectangle(image, new Point(r.x, r.y),
        			new Point(r.x + r.width, r.y + r.height), new Scalar(255, 45, 34));	     
        	
	        BufferedImage croppedTemp = in.getSubimage(r.x,r.y,r.width,r.height);
	        
	        byte[] pixels = ((DataBufferByte) croppedTemp.getRaster().getDataBuffer()).getData();
	        CascadeClassifier testClass = new CascadeClassifier(FaceClassifier.class.getResource("haarcascade_frontalface_alt.xml").getPath());
	       // Mat image = Highgui.	
	        Mat cropped = new Mat(croppedTemp.getHeight(),croppedTemp.getWidth(), CvType.CV_8UC3);
	        cropped.put(0, 0, pixels);
	             
	        Main = reloadXml("haarcascade_mcs_mouth");
	        Main.detectMultiScale(cropped, mouthDetections, 2, 14, 1, new Size(10,10), cropped.size());
	        Main = reloadXml("haarcascade_mcs_eyepair_small");
	        Main.detectMultiScale(cropped, eyeDetections);
	        
	        for (Rect rect : eyeDetections.toArray()) {
	            Core.rectangle(cropped, new Point(rect.x, rect.y), 
	            		new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(255, 0, 0));
	        	System.out.println(String.format("%s %s",rect.x+rect.width/2.,rect.y+rect.height/2.));
	        }
	        
	        for (Rect rect : mouthDetections.toArray()){ 
	            Core.rectangle(cropped, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 0,255));
	        	System.out.println(String.format("%s %s",rect.x+rect.width/2.,rect.y+rect.height/2.));
	        }
	        
	        String filename = "output"+i+".png";
	        System.out.println(String.format("Writing %s", filename));
	        Highgui.imwrite(filename, cropped);
	        i++;
        }*/
        
       // Highgui.imwrite("ans.png", image);
        
        List<Rect> ansArrayList = new ArrayList<Rect>(faceDetections.toList());
        
        System.out.println("Finished");
		return ansArrayList;
    }

    private CascadeClassifier reloadXml(String name)
    {
    	CascadeClassifier classifier =  new CascadeClassifier();
    	if(!classifier.load("res/haarcascades/"+name+".xml"))
    		System.out.println("bad");
    	return classifier;
    }
    
    public void reloadImg(BufferedImage bufferedImage)
    {
    	byte[] pixels = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
        processingImg = new Mat(bufferedImage.getHeight(),
        		bufferedImage.getWidth(), CvType.CV_8UC3);
        processingImgBuf = bufferedImage;
    }
    
    private void arrayToImg(ArrayList<ArrayList<Pixel>> mas,int width, int height) throws IOException {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int k = 0;
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                img.setRGB(i, j, mas.get(i).get(j).all );
            }
        }
       javax.imageio.ImageIO.write(img, "jpeg", new File("code.jpeg"));
    }
    
    private MatOfRect findAllFaces(CascadeClassifier Main, Mat image)
    {
    	MatOfRect faces = new MatOfRect();
    	ArrayList<String> xmls = new ArrayList<String>();
    	xmls.add("haarcascade_frontalface_default");
    	xmls.add("haarcascade_frontalface_alt");
    	xmls.add("haarcascade_frontalface_alt2");
    	xmls.add("haarcascade_frontalface_alt_tree");
    	xmls.add("haarcascade_profileface");
    	MatOfRect faceDetections = new MatOfRect();
    	
    	for(String name: xmls)
    	{ 	       
            Main = reloadXml(name);
            MatOfRect faceDetectionsTemp = new MatOfRect();       
            Main.detectMultiScale(image, faceDetectionsTemp, 2, 4, 0, new Size(100,100), image.size()); 
            faceDetections = merge(faceDetections,faceDetectionsTemp);
            System.out.println(String.format("%s faces detected",faceDetections.toArray().length));
    	}
    	return faceDetections;
    }
    
    public List<BufferedImage> getCropFaces()
    {
    	if(cropFaces==null)
    		cropFaces = new ArrayList<BufferedImage>();
    	return cropFaces;
    }
    
    private MatOfRect merge(MatOfRect a,MatOfRect b)
    {
    	ArrayList<Rect> mas = new ArrayList<Rect>();
         for(Rect r: a.toArray())
         {
        	 mas.add(r);
         }
         
         for(Rect r: b.toArray())
         {
        	 boolean f=true;
        	 for(Rect r2: mas)
        		 if(Math.sqrt(Math.pow(r2.x-r.x, 2.) + Math.pow(r2.y-r.y,2.))<(r.width+r.height)/4. )
        		 {
        			 f=false;
        			 break;
        		 }
        	 if(f)
        	 {
        		 mas.add(r);
        	 }
         }
         
         Rect[] mas2 = new Rect[mas.size()];
         int i=0;
         for(Rect r: mas)
         {
        	 mas2[i]=r;
        	 i++;
         } 
         MatOfRect c;
         if(mas2.length>0)
        	 c = new MatOfRect(mas2);
         else
        	 c = new MatOfRect();
         return c;
    }
    
    public BufferedImage mat2Img(Mat in)
    {
        MatOfByte bytemat = new MatOfByte();

        Highgui.imencode(".jpg", in, bytemat);

        byte[] bytes = bytemat.toArray();

        InputStream inp = new ByteArrayInputStream(bytes);

        BufferedImage out = new BufferedImage(1,1,1);
		try {
			out = ImageIO.read(inp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return out;
    } 
    
}