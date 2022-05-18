package hcmute.edu.vn.foody_24.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import hcmute.edu.vn.foody_24.R;
import hcmute.edu.vn.foody_24.models.Diner;
import hcmute.edu.vn.foody_24.models.Food;
import hcmute.edu.vn.foody_24.service.DinerService;
import hcmute.edu.vn.foody_24.service.FoodService;

public class CreateData{

    public void createDB(){
        createDinner();
        createFood();
        Log.i("CREATE_DB","DONE CREATE DATA");
    }

    public void deleteDB(){

        SQLiteDatabase db = DatabaseHelper.getInstance().getWritableDatabase();
        String dbName = DatabaseHelper.getInstance().getDatabaseName();
        for (String tableName : DatabaseHelper.getInstance().getListTableName()) {
            //Object[] bindArg = {DatabaseHelper.getInstance().getDatabaseName(), tableName};
            db.execSQL("DROP TABLE " + tableName + ";");
        }
    }

    private static void createUser(){
        SQLiteDatabase db = DatabaseHelper.getInstance().getWritableDatabase();
        db.execSQL("insert into users values(null, 'admin', 'admin', " +
                "'1,Võ Văn Ngân, Linh Chiểu, Thủ đức, Tp.HCM', '0987654321');" );
    }

    private static void createDinner(){

        ArrayList<Diner> list = new ArrayList<>();

        list.add(new Diner(R.drawable.diner5, "Phúc Lộc Thọ"));
        list.add(new Diner(R.drawable.diner6, "Hẻm nhỏ 2"));
        list.add(new Diner(R.drawable.diner7, "Cơm Minh Thư 2"));
        list.add(new Diner(R.drawable.diner8, "Cơm tấm cô Ba"));

        list.add(new Diner(R.drawable.diner9, "Gió biển"));
        list.add(new Diner(R.drawable.diner5, "Nai vàng"));
        list.add(new Diner(R.drawable.diner6, "Hương biển"));
        list.add(new Diner(R.drawable.diner7, "TocoToco Kha Van Cân Cân Cân Cân"));

        list.add(new Diner(R.drawable.diner8, "Synary UTE"));
        list.add(new Diner(R.drawable.diner9, "Panda Coffe"));
        list.add(new Diner(R.drawable.diner5, "Lotte Võ Văn Ngân"));
        list.add(new Diner(R.drawable.diner6, "Ông bầu"));

        DinerService dinerService = new DinerService();
        for (Diner d : list){
            dinerService.insert(d);
        }

    }

    private static void createFood(){
        ArrayList<Food> list = new ArrayList<>();
        //diner1

        for (int i = 1; i <= 12; i++){
            list.add(new Food(R.drawable.food1_diner1,"Bún đậu mắm tôm Vip1", "Vị mới, siêu ngon, giá hời",12.5 , i));
            list.add(new Food(R.drawable.food2_diner1,"Bún đậu mắm tôm HaoHao", "Vị mới, siêu ngon, giá hời",8 , i));
            list.add(new Food(R.drawable.food3_diner1,"Bún đậu mắm tép", "Vị mới, siêu ngon, giá hời",11 , i));
            list.add(new Food(R.drawable.food4_diner1,"Bún tép mắm đậu", "Gấp đôi phô mai",7 , i));

            list.add(new Food(R.drawable.food01,"Pizza ngô", "Vị mới, siêu ngon, giá hời",12.5 , i));
            list.add(new Food(R.drawable.food02,"Pizza hải sản", "Vị mới, siêu ngon, giá hời",8 , i));
            list.add(new Food(R.drawable.food03,"Pizza bò viên", "Vị mới, siêu ngon, giá hời",11 , i));
            list.add(new Food(R.drawable.food04,"Pizza double", "Gấp đôi phô mai",7 , i));

            list.add(new Food(R.drawable.food01,"Pizza ngô", "Vị mới, siêu ngon, giá hời",12.5 , i));
            list.add(new Food(R.drawable.food02,"Pizza hải sản", "Vị mới, siêu ngon, giá hời",8 , i));
            list.add(new Food(R.drawable.food03,"Pizza bò viên", "Vị mới, siêu ngon, giá hời",11 , i));
            list.add(new Food(R.drawable.food04,"Pizza double", "Gấp đôi phô mai",7 , i));
        }

        FoodService foodService = new FoodService();
        for (Food f : list){
            foodService.insert(f);
        }


    }

}
