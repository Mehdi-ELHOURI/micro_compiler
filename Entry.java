public class Entry<T> {

    //Champs
    private String name;
    private String type;
    private T value;
    private boolean isConst;
    private int address;

    public Entry(String name, String type, T value, boolean isConst) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.isConst = isConst;
    }

    //

    public void setAddress(int address) {
        if (address >= 0)
            this.address = address;
    }

    //getters


    public int getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public T getValue() {
        return value;
    }

    public boolean isConst() {
        return isConst;
    }

    public int getSize(){
        switch (type){
            case "number" :
                return Double.SIZE;
            case "boolean" :
                return Integer.SIZE;
            case "string" :
                return ( (String) this.getValue() ).length() * Character.SIZE;
            default:return 0;
        }
    }

    @Override
    public String toString() {
        return "Entry{" +
                "adress=" + String.format("Ox%x",getAddress()) +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", value=" + value +
                ", isConst=" + isConst +
                ", size=" + getSize() +
                '}';
    }
}
