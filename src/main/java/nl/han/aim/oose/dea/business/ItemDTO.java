package nl.han.aim.oose.dea.business;

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
        // Check of de categorie bestaat.
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
    public boolean equals(Object itemDTOToCompare) {
        ItemDTO item = (ItemDTO) itemDTOToCompare;
        if (itemDTOToCompare==null) {
            return false;
        }
        return item.getSku().equals(getSku())
                && item.getCategory().equals(getCategory())
                && item.getTitle().equals(getTitle());
    }

}
