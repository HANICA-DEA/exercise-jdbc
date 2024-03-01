package nl.han.aim.oose.dea.domain;

public class ItemDTO {
    private String sku;
    private String category;
    private String title;

    public ItemDTO(){}

    public ItemDTO(String sku, String category, String title) {
        this.sku = sku;
        this.category = category;
        this.title = title;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Item{" +
                "sku='" + sku + '\'' +
                ", category='" + category + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        ItemDTO oAsItem = (ItemDTO) o;
        if (o==null) {
            return false;
        }
        return oAsItem.getSku().equals(getSku())
                && oAsItem.getCategory().equals(getCategory())
                && oAsItem.getTitle().equals(getTitle());
    }

}
