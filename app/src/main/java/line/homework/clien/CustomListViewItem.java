/*
 * 클래스 이름 : CustomListViewItem
 *  - 리스트뷰의 아이템에 대한 정의
 * 버전 정보
 *
 * 날짜 : 2018.01.11
 *
 */
package line.homework.clien;

public class CustomListViewItem {
    private String titleStr ; // 타이틀
    private String writerStr ; // 작성자
    private String viewsStr ; // 작성 시간

    public void setTitle(String title) {
        titleStr = title ;
    }
    public void setWriter(String writer) {
        writerStr = writer ;
    }
    public void setTimes(String times) {
        viewsStr = times ;
    }

    public String getTitle() {
        return this.titleStr ;
    }
    public String getWriter() {
        return this.writerStr ;
    }
    public String getTimes() {
        return this.viewsStr ;
    }
}