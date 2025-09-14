package murach.business;

import java.io.Serializable;
import java.util.ArrayList;

public class Cart implements Serializable {

    private ArrayList<LineItem> items;

    public Cart() {
        items = new ArrayList<LineItem>();
    }

    public ArrayList<LineItem> getItems() {
        return items;
    }

    public int getCount() {
        return items.size();
    }

    public void addItem(LineItem item) {
        String code = item.getProduct().getCode();
        int quantity = item.getQuantity();
        for (int i = 0; i < items.size(); i++) {
            LineItem lineItem = items.get(i);
            if (lineItem.getProduct().getCode().equals(code)) {
                // Cộng dồn số lượng
                lineItem.setQuantity(lineItem.getQuantity() + quantity);
                return;
            }
        }
        // Nếu mặt hàng chưa tồn tại, thêm mới
        items.add(item);
    }

    public void updateItemQuantity(LineItem item, int quantity) {
        String code = item.getProduct().getCode();
        for (int i = 0; i < items.size(); i++) {
            LineItem lineItem = items.get(i);
            if (lineItem.getProduct().getCode().equals(code)) {
                // Đặt số lượng cụ thể
                lineItem.setQuantity(quantity);
                return;
            }
        }
        // Nếu mặt hàng chưa tồn tại, thêm mới với số lượng được chỉ định
        item.setQuantity(quantity);
        items.add(item);
    }

    public void removeItem(LineItem item) {
        String code = item.getProduct().getCode();
        for (int i = 0; i < items.size(); i++) {
            LineItem lineItem = items.get(i);
            if (lineItem.getProduct().getCode().equals(code)) {
                items.remove(i);
                return;
            }
        }
    }
}