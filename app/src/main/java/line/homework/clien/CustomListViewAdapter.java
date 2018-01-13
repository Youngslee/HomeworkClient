/*
 * 클래스 이름 : CustomListViewAdapter
 *  - 커스텀 리스트뷰에 대한 연결 담당
 * 버전 정보
 *
 * 날짜 : 2018.01.11
 *
 */
package line.homework.clien;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import line.homework.R;



public class CustomListViewAdapter extends BaseAdapter {

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<CustomListViewItem> listViewItemList = new ArrayList<CustomListViewItem>() ;

    public CustomListViewAdapter() {}

    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.customlistview_item, parent, false);
        }

        TextView titleTextView = (TextView) convertView.findViewById(R.id.title) ;
        TextView writerTextView = (TextView) convertView.findViewById(R.id.writer) ;
        TextView viewsTextView = (TextView) convertView.findViewById(R.id.views) ;

        CustomListViewItem listViewItem = listViewItemList.get(position);

        titleTextView.setText(listViewItem.getTitle());
        writerTextView.setText(listViewItem.getWriter());
        viewsTextView.setText(listViewItem.getViews());

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가 함수
    public void addItem(String title, String writer, String views) {
        CustomListViewItem item = new CustomListViewItem();
        item.setTitle(title);
        item.setWriter(writer);
        item.setViews(views);

        listViewItemList.add(item);
    }
}
