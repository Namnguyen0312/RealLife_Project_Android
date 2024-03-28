package com.example.reallifeproject.utils;

import com.example.reallifeproject.R;
import com.example.reallifeproject.model.ItemModel;

import java.util.ArrayList;

public class ItemUtil {
    private static ItemUtil instance;
    private static ArrayList<ItemModel> allItems;

    private ItemUtil() {
        allItems = new ArrayList<>();
        initItem();;
    }

    private static void initItem(){
        //Sword 1-100
        allItems.add(new ItemModel(1, "Nhành cây", 499, 10, 0, 0, 0,0, "Không có", "Là một nhành gỗ được nhặt ven đường", "Tất cả", false, "Tất cả", "Kiếm", "Thường", R.drawable.wood_stick_icon));
        allItems.add(new ItemModel(2, "Kiếm vàng", 9999, 30, 30, 30, 30, 30, "Không có", "Thanh kiếm được bao bọc bởi vàng, được rèn nên bởi đôi tay của người thợ rèn giỏi nhất", "Tất cả", false, "Tất cả", "Kiếm", "Huyền thoại", R.drawable.kiem_vang));
        allItems.add(new ItemModel(3, "Kiếm lai", 1999, 20, 0, 10,0, 0, "Không có", "Thanh kiếm là tuyệt phẩm của 2 con người có 2 dòng máu khác nhau", "Tất cả", false, "Tất cả", "Kiếm", "Hiếm", R.drawable.kiem_lai));
        allItems.add(new ItemModel(4, "Kiếm của hội Hoa Hồng", 2599, 30, 0, 10,0, 0, "Không có", "Thanh kiếm được truyền qua các đời của hội Hoa Hồng", "Tất cả", false, "Tất cả", "Kiếm", "Huyền thoại", R.drawable.kiem_hoi_hoa_hong));
        allItems.add(new ItemModel(5, "Kiếm đại", 2199, 30, 5, 0,0, 0, "Không có", "Thanh đại kiếm tuy nặng nhưng sát thương cao", "Tất cả", false, "Tất cả", "Kiếm", "Hiếm", R.drawable.dai_kiem));
        allItems.add(new ItemModel(6, "Kiếm thiên sứ", 3599, 40, 5, 10,0, 5, "Không có", "Đại diện cho những thiên sứ bầu trời, thanh kiếm mang sức mạnh phi thường", "Tất cả", false, "Tất cả", "Kiếm", "Sử thi", R.drawable.kiem_thien_su));
        allItems.add(new ItemModel(7, "Kiếm thánh", 4999, 100, 10, 10,10, 10, "Không có", "Cây kiếm là biểu tượng của hiệp sĩ mệnh danh là Kiếm Thánh", "Tất cả", false, "Tất cả", "Kiếm", "Sử thi", R.drawable.kiem_thanh));
        allItems.add(new ItemModel(8, "Kiếm khe nứt", 2199, 10, 25, 0,0, 0, "Không có", "Thanh kiếm được tôi luyện nhiều năm dưới khe nứt", "Tất cả", false, "Tất cả", "Kiếm", "Hiếm", R.drawable.kiem_khe_nut));
        allItems.add(new ItemModel(9, "Kiếm sát bá", 2499, 50, -10, 0,0, 0, "Không có", "Người dân đồn rằng thanh kiếm này bị nguyền rủa, người cầm nó sẽ luôn tỏa ra sát khí nồng nặc", "Tất cả", false, "Tất cả", "Kiếm", "Hiếm", R.drawable.kiem_sat_ba));
        allItems.add(new ItemModel(10, "Kiếm vô danh", 4999, 150, 0, 0,0, 0, "Không có", "Vua vô danh đã để lại thanh kiếm này, người sở hữu sẽ có được sức mạnh vô biên", "Tất cả", false, "Tất cả", "Kiếm", "Sử thi", R.drawable.kiem_vo_danh));
        allItems.add(new ItemModel(11, "Kiếm diệt rồng", 7999, 200, 0, 50,0, 30, "Không có", "Khả năng giết rồng chỉ với vài nhát chém, đó chính là sức mạnh của thanh kiếm dùng để diệt rồng", "Tất cả", false, "Tất cả", "Kiếm", "Huyền thoại", R.drawable.kiem_diet_rong));
        allItems.add(new ItemModel(12, "Dao thông dụng", 799, 10, 0, 0,0, 10, "Không có", "Loại dao phổ biến với các chiến binh", "Tất cả", false, "Tất cả", "Kiếm", "Thường", R.drawable.dao_thong_dung));
        allItems.add(new ItemModel(13, "Kiếm không xác định", 9999, 1, 0, 0,0, 0, "Không có", "???", "Tất cả", false, "Tất cả", "Kiếm", "Thường", R.drawable.kiem_khong_xac_dinh));
        allItems.add(new ItemModel(14, "Dao nhọn", 1999, 15, 0, 0,0, 15, "Không có", "Loại dao có thể dùng trong mọi trận chiến", "Tất cả", false, "Tất cả", "Kiếm", "Hiếm", R.drawable.dao_nhon));
        allItems.add(new ItemModel(15, "Gươm hiệp sĩ", 1999, 40, 0, 0,0, -10, "Không có", "Một thanh gươm nhuốm máu", "Tất cả", false, "Tất cả", "Kiếm", "Hiếm", R.drawable.guom_hiep_si));
        allItems.add(new ItemModel(16, "Kiếm diệt quỷ", 5199, 150, -30, 30,0, -10, "Không có", "Thanh kiếm mà ngay cả vua quỷ cũng dè chừng", "Tất cả", false, "Tất cả", "Kiếm", "Sử thi", R.drawable.kiem_diet_quy));
        allItems.add(new ItemModel(17, "Kiếm linh", 8199, 100, 30, 100,0, 30, "Không có", "Thanh kiếm chứa đựng những linh hồn vất vưởng, mang sức mạnh thần bí", "Tất cả", false, "Tất cả", "Kiếm", "Huyền thoại", R.drawable.kiem_linh));
        allItems.add(new ItemModel(18, "Kiếm của vua ăn mày", 7599, 111, 0, 111,0, 0, "Không có", "Thiên hạ đồn rằng có người trông như ăn mày nhưng khi đi đến đâu người ta cũng đều kính nể, và đây chính là thanh kiếm của ông ta", "Tất cả", false, "Tất cả", "Kiếm", "Huyền thoại", R.drawable.kiem_cua_an_may));
        allItems.add(new ItemModel(19, "Kiếm bá tước", 2599, 50, 10, 10,0, 10, "Không có", "Là bá tước của một vùng, thanh kiếm của ông có thể bình thiên hạ", "Tất cả", false, "Tất cả", "Kiếm", "Hiếm", R.drawable.kiem_ba_tuoc));
        allItems.add(new ItemModel(20, "Kiếm thép", 2599, 40, 20, 0,0, 10, "Không có", "Thanh kiếm làm từ thép gang, rất chắc chắn và sắc lẹm", "Tất cả", false, "Tất cả", "Kiếm", "Hiếm", R.drawable.kiem_thep));
        allItems.add(new ItemModel(21, "Kiếm phá khung", 3099, 35, 10, 35,0, 10, "Không có", "Với cấu trúc được thiết kế độc lập, thanh kiếm mang nhiều tiềm năng", "Tất cả", false, "Tất cả", "Kiếm", "Hiếm", R.drawable.kiem_pha_khung));
        //Shield 101-200
        allItems.add(new ItemModel(101, "Khiên tròn", 499, 0, 10, 0,0, 0, "Không có", "Loại khiên phù hợp với tất cả mọi người", "Tất cả", false, "Tất cả", "Khiên", "Thường", R.drawable.khien_tron));
        allItems.add(new ItemModel(102, "Khiên tròn thông dụng", 599, 0, 15, 0,0, 0, "Không có", "Chiếc khiên dành cho những chiến binh dũng cảm", "Tất cả", false, "Tất cả", "Khiên", "Thường", R.drawable.khien_tron_thong_dung));
        allItems.add(new ItemModel(103, "Khiên tròn sắt", 1599, 0, 20, 0,5, 0, "Không có", "Một chiếc khiên được tôi luyện rất chắc chắn", "Tất cả", false, "Tất cả", "Khiên", "Thường", R.drawable.khien_tron_sat));
        allItems.add(new ItemModel(104, "Khiên lưới", 2799, 10, 30, 0,0, 20, "Không có", "Với thiết kế độc đáo, chính giữa được kết bằng các dây thép giúp chống lại mọi loại vũ khí", "Tất cả", false, "Tất cả", "Khiên", "Hiếm", R.drawable.khien_luoi));
        allItems.add(new ItemModel(105, "Khiên của hội Hoa Hồng", 2599, 10, 30, 10,5, -5, "Không có", "Chiếc khiên được người đứng đầu hội Hoa Hồng đời 6 để lại", "Tất cả", false, "Tất cả", "Khiên", "Hiếm", R.drawable.khien_cua_hoi_hoa_hong));
        allItems.add(new ItemModel(106, "Khiên thiên sứ", 3199, 20, 40, 10,5, -10, "Không có", "Chiếc khiên có sứ mệnh cứu rỗi thế giới", "Tất cả", false, "Tất cả", "Khiên", "Sử thi", R.drawable.khien_thien_su));
        allItems.add(new ItemModel(107, "Khiên thánh", 4999, 0, 50, 0,50, -15, "Không có", "Mỗi thế hệ chỉ có một Khiên thánh, và đây là một trong những chiếc khiên vĩ đại của người vĩ đại", "Tất cả", false, "Tất cả", "Khiên", "Sử thi", R.drawable.khien_thanh));
        allItems.add(new ItemModel(108, "Khiên khe nứt", 2199, 0, 20, 10,10, -5, "Không có", "Khe nứt đã để lại chiếc khiên vĩnh hằng này", "Tất cả", false, "Tất cả", "Khiên", "Hiếm", R.drawable.khien_khe_nut));
        allItems.add(new ItemModel(109, "Khiên lửa", 2499, 10, 30, 10,0, -5, "Không có", "Tuyên truyền rằng đây là chiếc khiên của quỷ lửa, loài quỷ mang đến nỗi khiếp sợ mỗi khi nhắc tên", "Tất cả", false, "Tất cả", "Khiên", "Hiếm", R.drawable.khien_lua));
        allItems.add(new ItemModel(110, "Khiên vô danh", 4999, 0, 100, 0,50, -15, "Không có", "Mảnh ghép để kế thừa sức mạnh của vị vua vô danh", "Tất cả", false, "Tất cả", "Khiên", "Sử thi", R.drawable.khien_vo_danh));
        allItems.add(new ItemModel(111, "Khiên thập tự", 3199, 0, 0, 0,100, -10, "Không có", "Loại khiên tốt nhất để chống lại quỷ dữ", "Tất cả", false, "Tất cả", "Khiên", "Hiếm", R.drawable.khien_thap_tu));
        allItems.add(new ItemModel(112, "Khiên vàng", 9999, 30, 30, 30,30, 30, "Không có", "Mọi đòn tấn công đói với chiếc khiên này là vô dụng", "Tất cả", false, "Tất cả", "Khiên", "Huyền thoại", R.drawable.khien_vang));
        allItems.add(new ItemModel(113, "Khiên không xác định", 9999, 0, 1, 0,1, 0, "Không có", "???", "Tất cả", false, "Tất cả", "Khiên", "Thường", R.drawable.khien_khong_xac_dinh));
        allItems.add(new ItemModel(114, "Khiên sừng", 3599, 10, 40, 0,30, -10, "Không có", "Khiên làm từ sừng bò mặt quỷ", "Tất cả", false, "Tất cả", "Khiên", "Sử thi", R.drawable.khien_sung));
        allItems.add(new ItemModel(115, "Khiên sắt", 2599, 0, 50, 0,0, -5, "Không có", "Khiên sắt, chống gỉ, rất chắc chắn", "Tất cả", false, "Tất cả", "Khiên", "Hiếm", R.drawable.khien_sat));
        allItems.add(new ItemModel(116, "Khiên ghép", 5199, 0, 80, 10, 70, -15, "Không có", "Bằng cách ghép lại các di vật của từng đời Vua, chiếc khiên này ẩn chứa sức mạnh to lớn", "Tất cả", false, "Tất cả", "Khiên", "Sử thi", R.drawable.khien_ghep));
        allItems.add(new ItemModel(117, "Khiên rắn", 8199, 100, 100, 20,50, -20, "Không có", "Khiên làm từ da của Vua rắn", "Tất cả", false, "Tất cả", "Khiên", "Huyền thoại", R.drawable.khien_ran));
        allItems.add(new ItemModel(118, "Khiên nhọn", 7599, 170, 30, 30,30, -20, "Không có", "Chiếc khiên được mài dũa rất nhọn, có sát thương cũng như độ bền vô đối", "Tất cả", false, "Tất cả", "Khiên", "Huyền thoại", R.drawable.khien_nhon));
        allItems.add(new ItemModel(119, "Khiên bộ lạc", 2799, 0, 50, 20,0, -10, "Không có", "Là bá tước của một vùng, thanh kiếm của ông có thể bình thiên hạ", "Tất cả", false, "Tất cả", "Khiên", "Hiếm", R.drawable.khien_bo_lac));
        //Magic Wand 201-300
        allItems.add(new ItemModel(201, "Gậy phép sơ cấp", 1599, 0, 0, 15, 0,0, "Không có", "Kỹ năng: Tăng cường", "Tất cả", false, "Pháp sư", "Gậy phép", "Thường", R.drawable.gay_phep_so_cap));
        allItems.add(new ItemModel(202, "Gậy ánh sáng", 2599, 0, 0, 25, 0, 0, "Không có", "Kỹ năng: Chói sáng", "Tất cả", false, "Pháp sư", "Gậy phép", "Hiếm", R.drawable.gay_anh_sang));
        allItems.add(new ItemModel(203, "Gậy băng", 4999, 0, 0, 40,10, 0, "Không có", "Kỹ năng: Khiên băng", "Tất cả", false, "Pháp sư", "Gậy phép", "Sử thi", R.drawable.gay_bang));
        allItems.add(new ItemModel(204, "Gậy bóng tối", 2599, 0, 0, 25,0, 0, "Không có", "Kỹ năng: Màn dêm", "Tất cả", false, "Pháp sư", "Gậy phép", "Hiếm", R.drawable.gay_bong_toi));
        allItems.add(new ItemModel(205, "Gậy cổ thụ ngàn năm", 5999, 0, 5, 70,5, 0, "Không có", "Kỹ năng: Ngàn cân", "Tất cả", false, "Pháp sư", "Gậy phép", "Sử thi", R.drawable.gay_co_thu_ngan_nam));
        allItems.add(new ItemModel(206, "Gậy của hội Hoa Hồng", 2599, 0, 5, 15,5, 0, "Không có", "Kỹ năng: Tăng cường", "Tất cả", false, "Pháp sư", "Gậy phép", "Hiếm", R.drawable.gay_cua_hoi_hoa_hong));
        allItems.add(new ItemModel(207, "Gậy của thần chết", 7599, -10, -10, 150,-10, -10, "Không có", "Kỹ năng: Đoạt mệnh", "Tất cả", false, "Pháp sư", "Gậy phép", "Huyền thoại", R.drawable.gay_cua_than_chet));
        allItems.add(new ItemModel(208, "Gậy không xác định", 9999, 0, 0, 1,0, 0, "Không có", "Kỹ năng: ???", "Tất cả", false, "Pháp sư", "Gậy phép", "Thường", R.drawable.gay_khong_xac_dinh));
        allItems.add(new ItemModel(209, "Gậy lấp lánh", 4599, 0, 0, 45,0, 0, "Không có", "Kỹ năng: Bất hoại", "Tất cả", false, "Pháp sư", "Gậy phép", "Sử thi", R.drawable.gay_lap_lanh));
        allItems.add(new ItemModel(210, "Gậy lửa", 4999, 0, 0, 50,0, 0, "Không có", "Kỹ năng: Cầu lửa", "Tất cả", false, "Pháp sư", "Gậy phép", "Sử thi", R.drawable.gay_lua));
        allItems.add(new ItemModel(211, "Gậy ma pháp sư", 5199, 0, 0, 60,0, 0, "Không có", "Kỹ năng: Nguyền rủa", "Tất cả", false, "Pháp sư", "Gậy phép", "Sử thi", R.drawable.gay_ma_phap_su));
        allItems.add(new ItemModel(212, "Gậy móng ngựa", 1599, 0, 0, 10,0, 5, "Không có", "Kỹ năng: Nhanh nhẹn", "Tất cả", false, "Pháp sư", "Gậy phép", "Thường", R.drawable.gay_mong_ngua));
        allItems.add(new ItemModel(213, "Gậy pha lê", 2599, 0, 0, 25,0, 0, "Không có", "Kỹ năng: Cầu pha lê", "Tất cả", false, "Pháp sư", "Gậy phép", "Hiếm", R.drawable.gay_pha_le));
        allItems.add(new ItemModel(214, "Gậy phép trung cấp", 4199, 0, 0, 40,0, 0, "Không có", "Kỹ năng: Hố đen", "Tất cả", false, "Pháp sư", "Gậy phép", "Sử thi", R.drawable.gay_phap_su_trung_cap));
        allItems.add(new ItemModel(215, "Gậy phép cao cấp", 6999, 0, 0, 100,0, 0, "Không có", "Kỹ năng: Vụ nổ vũ trụ", "Tất cả", false, "Pháp sư", "Gậy phép", "Huyền thoại", R.drawable.gay_phep_cao_cap));
        allItems.add(new ItemModel(216, "Gậy thánh", 8999, 0, 0, 150,0, 0, "Không có", "Kỹ năng: Hồi sinh", "Tất cả", false, "Pháp sư", "Gậy phép", "Huyền thoại", R.drawable.gay_thanh));
        allItems.add(new ItemModel(217, "Gậy thiên sứ", 6666, 0, 6, 66,6, 0, "Không có", "Kỹ năng: Hồi máu", "Tất cả", false, "Pháp sư", "Gậy phép", "Sử thi", R.drawable.gay_thien_su));
        allItems.add(new ItemModel(218, "Gậy thời tiết", 3599, 0, 0, 40,0, 0, "Không có", "Kỹ năng: Tạo mưa", "Tất cả", false, "Pháp sư", "Gậy phép", "Hiếm", R.drawable.gay_thoi_tiet));
        allItems.add(new ItemModel(219, "Gậy dịch chuyển", 5199, 0, 0, 60,0, 0, "Không có", "Kỹ năng: Dịch chuyển", "Tất cả", false, "Pháp sư", "Gậy phép", "Sử thi", R.drawable.gay_tien_tri));
    }

    public static ItemUtil getInstance(){
        instance = new ItemUtil();
        return instance;
    }
    public static ArrayList<ItemModel> getItemModels() {
        return allItems;
    }
}
