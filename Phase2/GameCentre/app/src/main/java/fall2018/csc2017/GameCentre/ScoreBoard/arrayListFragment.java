//package fall2018.csc2017.GameCentre;
//
//import android.support.v4.app.ListFragment;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//import android.widget.Toast;
//
//public class arrayListFragment extends ListFragment {
//
//    int mNum;
//
//    static arrayListFragment newInstance(int num) {
//        arrayListFragment frag = new arrayListFragment();
//
//        Bundle args = new Bundle();
//        args.putInt("num", num);
//        frag.setArguments(args);
//
//        return frag;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mNum = getArguments() != null ? getArguments().getInt("num") : 1;
//    }
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        //setListAdapter(new ArrayAdapter<String>(getActivity(),
//         //       android.R.layout.simple_list_item_1, Cheeses.sCheeseStrings));
//    }
//
//}
