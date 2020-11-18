/**
 * 
 * Board와 Blank, Marker를 이용하여 판을 구성하는 테스트를 위한 클래스 입니다.
 * 체커의 게임판 (8 * 8)을 예시로 하여 구성되었고, 실제 게임의 구성은 구현하지 않고, Blank의 구성, 각 Blank간의 연결,
 * 게임판 출력 등의 기능들만 예시로 담았습니다.
 * 실제 게임 보드판을 만들 때 참고하시면 더 편하게 만들 수 있을 것 같습니다.
 * 
 */

package board;

import java.util.ArrayList;
import marker.Marker;

public class BoardExample extends Board { // Board클래스를 상속받도록 하였습니다.
	private ArrayList<Blank<Marker>> blank = new ArrayList<Blank<Marker>>();
	
	public BoardExample() { //체커의 게임판을 구성해보는 걸로 예시를 듭니다. 8 * 8의 게임 보드판이지만, 그 중 실제 게임말이 올라갈 수 있는 칸은 64가 아니라 32칸입니다. 따라서 32개의 Blank를 갖도록 설계하였습니다.
		super(32);
		
		for(int i = 0; i < 32; i++)
			blank.add(new Blank<Marker>(0, null));
		
		for(int i = 0; i < 7; i++) {
			super.makeConnection(4 * i, 4 * i + 4);
			super.makeConnection(4 * i + 4, 4 * i);
			super.makeConnection(4 * i + 1, 4 * i + 5);
			super.makeConnection(4 * i + 5, 4 * i + 1);
			super.makeConnection(4 * i + 2, 4 * i + 6);
			super.makeConnection(4 * i + 6, 4 * i + 2);
			super.makeConnection(4 * i + 3, 4 * i + 7);
			super.makeConnection(4 * i + 7, 4 * i + 3);
		}
		for(int i = 0; i < 4; i++) {
			super.makeConnection(8 * i, 8 * i + 5);
			super.makeConnection(8 * i + 5, 8 * i);
			super.makeConnection(8 * i + 1, 8 * i + 6);
			super.makeConnection(8 * i + 6, 8 * i + 1);
			super.makeConnection(8 * i + 2, 8 * i + 7);
			super.makeConnection(8 * i + 7, 8 * i + 2);
		}
	}
	
	@Override
	public boolean isConnected(int a, int b) { return super.isConnected(a, b); }
	@Override
	public String toString() { //사용자에게 보드의 상태를 보여줄 때 사용하는 매소드. 단순 테스트용으로 급하게 만들었기 때문에 수정이 많이 필요함.
		String output = "";
		for(int i = 0; i < 32; i++) {
			String block;
			if(i < 4 || (i >= 8 && i < 12) || (i >= 16 && i < 20) || (i >= 24 && i < 28)) output += " □";
			
			if(blank.get(i).isEmpty() == false) {
				if(((Marker)blank.get(i).getData()).getPlayer() == 0) {
					if(i < 4 || (i >= 8 && i < 12) || (i >= 16 && i < 20) || (i >= 24 && i < 28))
						block = " A";
					else
						block = "A ";
				}
				else {
					if(i < 4 || (i >= 8 && i < 12) || (i >= 16 && i < 20) || (i >= 24 && i < 28))
						block = " B";
					else
						block = "B ";
				}
			}
			else {
				if (i < 10) block = " " + i;
				else block = "" + i;
			}
			output += block;
			
			if((i >= 4 && i < 8) || (i >= 12 && i < 16) || (i >= 20 && i < 24) || (i >= 28 && i < 32)) output += " □";
			if(i == 3 || i == 7 || i == 11 || i == 15 || i == 19 || i == 23 || i == 27 || i == 31) output += "\n";
		}
		return output;
	}
	
	public void onBoard(Marker marker, int position/*blank 상의 위치*/, int index/*전체 게임말 중, 이 게임말이 갖는 index번호*/) {
		//지정된 위치의 Blank에 Marker를 올리기 위한 매소드. 게임을 시작할 때, 게임의 메인함수에서 기본적으로 올라가 있어야 하는 칸에 Marker를 올립니다. 
		blank.set(position, new Blank<Marker>(index, marker));
	}
	
	public boolean isOnBoard(int position) { //지정된 위치에 해당하는 Blank에 Marker가 있는지 없는지를 확인하기 위한 매소드.
		if (blank.get(position).getData() != null) return true;
		else return false;
	}
	
	public void move(int position, int targetPosition) { //Blank에 올라가 있는 Marker를 다른 Blank로 옮기기 위한 매소드.
		if (isOnBoard(position) == false || isOnBoard(targetPosition) == true) {
			System.out.println("Error in BoardExample.move;one of selected blanks is already filled or empty.");
			return;
		}
		if (this.isConnected(position, targetPosition) == false) {
			System.out.println("Error in BoardExample.move;selected blanks are not connected.");
			return;
		}
		
		//아래부터 난해한 부분. ArrayList내부의 두 값을 서로 바꿔주기 위한 작업.
		
		//1. 각 Blank안의 'data<Marker>'들과 Blank를 'markerTemp', 'blankTemp'라고하는 임시 변수에 넣어준다.
		Marker markerTemp_start = blank.get(position).getData();
		Marker markerTemp_target = blank.get(targetPosition).getData();
		Blank<Marker> blankTemp_start = blank.get(position);
		Blank<Marker> blankTemp_target = blank.get(targetPosition);
		//2. 각 Blank 안에 data들을 서로 교환하여 넣어준다.
		blankTemp_start.setData(markerTemp_target);
		blankTemp_target.setData(markerTemp_start);
		//3. data가 바뀐 Blank들을 BoardExample의 인스턴스인 'blank(:ArrayList<Blank>)'안에 넣어준다.
		blank.set(position, blankTemp_start);
		blank.set(targetPosition, blankTemp_target);
		//4. Blank안의 data값 교환(=Marker의 이동)이 성공적으로 이루어진다.
		
		//이렇게 코드가 난해하게 작성된 이유는, ArrayList는 직접적으로 index번호만을 가지고 내부의 자료에 접근할 수 없기 때문.
		//따라서, ArrayList에서 자료를 뽑아낸 다음, 뽑아낸 자료의 내부 값을 가져와 서로 바꿔주고, 변경된 자료를 다시 ArrayList안에 넣어주는 방식.
		
		//Q. 그러면 ArrayList가 아니라 그냥 배열을 사용하면 안되는가?
		//A. Generic Type의 경우, 배열 선언이 불가능하기 때문에  ArrayList를 사용할 수 밖에 없음.
		System.out.println("Movement Completed Successfully.");
	}
	
}
