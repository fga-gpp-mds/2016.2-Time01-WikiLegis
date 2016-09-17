package gppmds.wikilegis.view;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import gppmds.wikilegis.R;
import gppmds.wikilegis.controller.BillComparatorDate;
import gppmds.wikilegis.controller.BillComparatorProposals;
import gppmds.wikilegis.controller.BillController;
import gppmds.wikilegis.controller.SegmentController;
import gppmds.wikilegis.exception.BillException;
import gppmds.wikilegis.exception.SegmentException;
import gppmds.wikilegis.model.Bill;
import gppmds.wikilegis.model.Segment;

public class MainActivity extends AppCompatActivity {

    public static List<Segment> listSegment;
    public static BillController billController;
    public static SegmentController segmentController;
    public static Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) this.findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);


        RecyclerView recycler_view = (RecyclerView)findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        recycler_view.setLayoutManager(linearLayoutManager);

        segmentController = SegmentController.getInstance(getBaseContext());


        billController = new BillController(this);


        try {
            segmentController.initControllerSegments();
        } catch (SegmentException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            billController.initControllerBills();
        } catch (BillException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (SegmentException e) {
            e.printStackTrace();
        }
        listSegment = segmentController.getAllSegments();

        List<Bill> billList = billController.getAllBills();

        billList = filterigForStatusPublished();

        for(int i=0; i<billList.size(); i++) {
            Log.d("Id", billList.get(i).getTitle());
            Log.d("N", String.valueOf(billList.get(i).getNumberOfPrposals()));
        }

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(billList);
        recycler_view.setAdapter(adapter);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_deslogged, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_search:
                // User chose the "Search" card_bill, show field of search...
                return true;

            case R.id.action_profile:
                // User choose first icon.
                return true;

            case R.id.action_exit_account:
                //Deslog user
                return true;

            case R.id.action_filtering:
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public List<Bill> filterigForStatusClosed(){
        List<Bill> billListWithStatusClosed= new ArrayList<Bill>();

        List<Bill> list = null;

        list = billController.getAllBills();

        for(int index = 0 ; index<list.size();index++){
            if(list.get(index).getStatus().equals("closed")){
                billListWithStatusClosed.add(list.get(index));
            }
        }
        return billListWithStatusClosed;
    }

    public List<Bill> filterigForStatusPublished(){
        List<Bill> billListWithStatusPublished= new ArrayList<Bill>();

        List<Bill> list = null;

        list = billController.getAllBills();

        for(int index = 0 ; index<list.size();index++){
            if(list.get(index).getStatus().equals("published")){
                billListWithStatusPublished.add(list.get(index));
            }
        }
        return billListWithStatusPublished;
    }

    public static List<Bill> filtringForNumberOfProposals(List<Bill> billList) {
        BillComparatorProposals billComparatorDProposals = new BillComparatorProposals();
        Collections.sort(billList,billComparatorDProposals);
        return  billList;
    }

    public List<Bill> filtringForDate(List<Bill> billList){
        BillComparatorDate comparator = new BillComparatorDate();
        Collections.sort(billList,comparator);
        return billList;

    }

}