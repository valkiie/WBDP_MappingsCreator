public class WBDP {
    String name;
    int length;
    boolean isHeader;

    public WBDP(String name, int length, boolean isHeader) {
        this.name = name;
        this.length = length;
        this.isHeader = isHeader;
    }
    public WBDP(String name, int length) {
        this.name = name;
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    @Override
    public String toString() {
        return "WBDP{" +
                "name='" + name + '\'' +
                ", length=" + length +
                ", isHeader=" + isHeader +
                '}';
    }
}
