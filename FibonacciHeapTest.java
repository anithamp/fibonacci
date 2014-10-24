import java.util.*;
import java.io.*;

 

/* Fibonacci Heap Node **/

class FibonacciHeapNode

{
    FibonacciHeapNode child, left, right, parent;    
    int element;
    int degree=0;
    Boolean marked=false;  
	
 
/** Constructor **/

    public FibonacciHeapNode(int element)
    {
        this.right = this;

        this.left = this;

        this.element = element;

    } 
  
}

 

/** Class FibonacciHeap **/

class FibonacciHeap

{

    private FibonacciHeapNode root;

    private int count;  
	

    /** Constructor **/

    public FibonacciHeap()

    {

        root = null;

        count = 0;

    }

    public int size()
    {
	return count;
    } 

    /** Check if heap is empty **/

    public boolean isEmpty()
    {

        return root == null;

    }

 

    /** Make heap empty **/ 

    public void clear()

    {

        root = null;

        count = 0;

    }

 

    /** Function to insert **/

    public void insert(int element)

    {

        FibonacciHeapNode node = new FibonacciHeapNode(element);

        node.element = element;

        if (root != null) 

        {

            node.left = root;

            node.right = root.right;

            root.right = node;

            node.right.left = node;

            if (element < root.element) 

                root = node;            

        }

        else 

            root = node;

        count++;

    }   
// search min element
    FibonacciHeapNode x=null;
   

    public FibonacciHeapNode extractMin()

    {

         FibonacciHeap H1=this;//new FibonacciHeap();
    
        
       // FibonacciHeap H2=null;       
        FibonacciHeapNode ptr = root;

      //  FibonacciHeapNode x;
	x=root;
           

        ptr = ptr.right;

        while (ptr != root && ptr.right != null)
	{

            if(ptr.element<x.element)
            {
                x=ptr;
            }
            else
                ptr = ptr.right;

	}

       

//extract min

					

        if ( x.child != null )
        {
            FibonacciHeapNode child = x.child,minchild=x.child;

            while ( x.equals( child.parent ) )
            {
                FibonacciHeapNode nextChild = child.right;
                if(child.element<minchild.element)
                {
                    minchild=child;
                }
                child = nextChild;
            }
            H1.root=minchild ;
        }
        // remove minNode from root list
        x.left.right = x.right;
        x.right.left =x.left;
        // update minimum
        if ( x.right.equals( x ) )
        {
            x = null;
        }
        else
        {
            x = x.right;
         //   consolidate();
        }
        count--;
        root=x;
        return (x);
    }

     



/**HeapUnion**/
    public FibonacciHeap fibHeapUnion(FibonacciHeap H1,FibonacciHeap H2)
    {
        FibonacciHeap H=new FibonacciHeap();
        H.x=H1.x;
        link(H.root,H2.root);
        if(H1.x==null || H2.x!=null && H2.x.element<H1.x.element)
        {
            H.x=H2.x;
            H.count=H1.count+H2.count;
        }
        return(H);


    }

    protected void link( FibonacciHeapNode y, FibonacciHeapNode z )
    {
    // remove y from root list
        y.left.right = y.right;
        y.right.left = y.left;
        // make y a child of x
        if ( z.child == null ) // no previous children?
        {
            y.right = y;
            y.left = y;
        }
        else
        {
            y.left = z.child.left;
            y.right = z.child;
            y.right.left = y;
            y.left.right = y;
        }
        z.child = y;
        y.parent = z;
        z.degree++;
        y.marked = false;
    }






    protected void consolidate()
    {
        FibonacciHeap H1=this;//new FibonacciHeap();
        int arraySize = count + 1;

        ArrayList<FibonacciHeapNode> A = new ArrayList<FibonacciHeapNode>(
        arraySize );
        for ( int i = 0; i < arraySize; ++i )
        {
            A.add( null );
        }
        List<FibonacciHeapNode> rootNodes = new LinkedList<FibonacciHeapNode>();
        rootNodes.add( x );
        for ( FibonacciHeapNode n =x.right; !n.equals( x ); n = n.right )
        {
            rootNodes.add( n );
        }
        for ( FibonacciHeapNode node : rootNodes )
        {

            if ( node.parent != null )
            {
                continue;
            }
            int d = node.degree;
            while ( A.get( d ) != null )
            {
                FibonacciHeapNode y = A.get( d );

                if ( node.element> y.element  )
                {
                    FibonacciHeapNode tmp = node;
                    node = y;
                    y = tmp;
                }
                link( y, node );
                A.set( d, null );
                ++d;
            }
            A.set( d, node );
        }

        x = null;

        for ( FibonacciHeapNode node : A )
        {
            if ( node != null )
            {
                this.root=node;
            }
        }
        
       
    }




    public void decreaseKey( FibonacciHeapNode node,int newKey )
    {
        x=root;
           
        FibonacciHeapNode ptr = root;
        ptr = ptr.right;

        while (ptr != root && ptr.right != null)
	{

            if(ptr.element<x.element)
            {
                x=ptr;
            }
            else
                ptr = ptr.right;

	}

        if (  newKey> node.element  )
        {
            throw new RuntimeException( "Trying to decrease to a greater key" );
        }
        node.element = newKey;
        FibonacciHeapNode parent = node.parent;
        if ( parent != null &&  (node.element<parent.element)  )
        {
            cut( node, parent );
            cascadingCut( parent );
        }
        System.out.println(""+x.element);
        if (  node.element > x.element  )
        {
            x = node;
        }
    }
/**
* Internal helper function. This removes y's child x and moves x to the
* root list.
*/
    protected void cut( FibonacciHeapNode z, FibonacciHeapNode y )
    {
        FibonacciHeap H1=new FibonacciHeap();
        // remove x from child list of y
        z.left.right = z.right;
        z.right.left = z.left;
        if (z.right.equals(z) )
        {
            y.child = null;
        }
        else
        {
            y.child = z.right;
        }
        y.degree--;
        // add x to root list
        root=z;
    }
/**
* Internal helper function.
*/
    protected void cascadingCut( FibonacciHeapNode y )
    {
        FibonacciHeapNode parent = y.parent;
        if ( parent != null )
        {
            if ( !parent.marked )
            {
                parent.marked = true;
            }
            else
            {
                cut( y, parent );
                cascadingCut( parent );
            }
        }
    }


    public FibonacciHeapNode search(int key)

    {
     
        FibonacciHeapNode ptr = root;

        FibonacciHeapNode x;
	x=root;
           

        ptr = ptr.right;

        while (ptr != root && ptr.right != null)
	{

            if(ptr.element==key)
            {
                return(ptr);
            }
	}
        return ptr;
    }



 

    /** function to display **/

    public void display()

    {

        System.out.print("\nHeap = ");

        FibonacciHeapNode ptr = root;

        if (ptr == null)

        {

            System.out.print("Empty\n");

            return;

        }        

        do

        {

            System.out.print(ptr.element +" ");

            ptr = ptr.right;

        } while (ptr != root && ptr.right != null);

        System.out.println();

    } 

}    

 

/** Class FibonacciHeapTest **/

public class FibonacciHeapTest

{

    public static void main(String[] args)

    {

        Scanner scan = new Scanner(System.in);

        System.out.println("FibonacciHeap Test\n\n");        

        FibonacciHeap fh = new FibonacciHeap();

        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));

        char ch;

        /**  Perform FibonacciHeap operations  **/

        do    

        {

            System.out.println("\nFibonacciHeap Operations\n");

            System.out.println("1. insert element ");

            System.out.println("2. check empty");            

            System.out.println("3. clear");
            System.out.println("4. Deletion");
            System.out.println("5.Extract min element");
            System.out.println("6.Decreasing a key");


 

            int choice = scan.nextInt();            

            switch (choice)

            {

                case 1 : 

                    System.out.println("Enter element");

                    fh.insert( scan.nextInt() );                                    

                    break;                          

                case 2 : 

                    System.out.println("Empty status = "+ fh.isEmpty());

                    break;   

                case 3 : 

                    fh.clear();

                    break;  
                case 4:
                    try
                    {
                        System.out.println("enter the key to be deleted");
                        int newKey=Integer.parseInt(br.readLine());
                        FibonacciHeapNode h=fh.search(newKey);
                        fh.decreaseKey( h, -10 );
                        FibonacciHeapNode x= fh.extractMin();


                    }
                    catch(Exception e)
                    {
                        System.out.println("IOException"+e);
                        e.printStackTrace();
                    }   
                    break;  
                case 5:      
                    FibonacciHeapNode x= fh.extractMin();
                    break;
                 //   System.out.println("minimum element "+x.element); 
                case 6:
                    try
                    {
                        System.out.println("enter the key to be decreased");
                        int key=Integer.parseInt(br.readLine());
                        FibonacciHeapNode h=fh.search(key);
                        System.out.println("enter the new key value");
                        int newKey=Integer.parseInt(br.readLine());
                        fh.decreaseKey( h, newKey );
                       


                    }
                    catch(Exception e)
                    {
                        System.out.println("IOException"+e);
                        e.printStackTrace();
                    }   
                    break;  
                default : 

                    System.out.println("Wrong Entry \n ");

                    break;   

            }           

            fh.display();

 

            System.out.println("\nDo you want to continue (Type y or n) \n");

            ch = scan.next().charAt(0);                        

        } while (ch == 'Y'|| ch == 'y');  

    }

}
