package nam2626.codenation.com.drugwarring;

class DrugVO {
    private int id;
    private String name;
    private String content;

    public DrugVO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DrugVO(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public DrugVO(int id, String name, String content) {

        this.id = id;
        this.name = name;
        this.content = content;
    }

    @Override
    public String toString() {
        return "DrugVO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
