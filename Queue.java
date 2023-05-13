public class Queue {
    
    // array that holds all the values
    private Object[] array;

    // indices for start and end of queue
    private int start, end;

    // number of items in the queue
    private int num;

    // maximum length of queue
    private int length;

    /**
     * Constructor for the queue, makes an empty queue
     */
    public Queue () {
        array = new Object[length = 64];
        start = 0; end = 0;
        num = 0;
    }

    /**
     * 
     * @param o
     * @return
     */
    public Queue add(Object o) {
        if (start%length == (end+1)%length && num != 0) 
            enlarge();
        array[end = ++end%length] = o;
        ++num;
        return this;
    }

    /**
     * 
     * @return
     */
    public Object peek() {
        if (num == 0)
            return null;
        else 
            return array[start];
    }

    /**
     * 
     * @return
     */
    public Object remove() {
        if (num == 0) {
            return null;
        } else {
            Object o = array[start];
            start = ++start % length;
            --num;
            return o;
        }
    }

    /**
     * 
     * @return 
     */
    public int length() {
        return num;
    }

    /**
     * 
     * @return
     */
    private Queue enlarge() {
        Object[] newArray = new Object[length *= 2];
        for (int i = start; i < array.length; i++) 
            newArray[i] = array[i];
        array = newArray;
        return this;
    }

}
