# 유의사항

직접 사용할 목적으로 간단하게 만들어서 edge case가 다수 존재할 것으로 예상됩니다.

단순히 개인 업무 편의를 위해 작성한 코드여서 모든 edge case를 수정하지는 않을 것 같습니다.

오프라인 작업을 고려하여 필요한 의존성을 .jar로 직접 주입했습니다.

제 업무 특성 상 20만 row이상인 csv파일인 경우 10만row씩 개별 xlsx로 분할하여 저장되도록 설계했습니다.

단순한 구조(ex. 일반적인 DB데이터 구조) 파일을 변환 하는 데 사용 가능합니다.(셀 합침 등 복잡하게 구성되어 있는 Excel파일 변환은 불가능)

현재 코드상으로는 MS949로 변환. Config class의 ENCODING_NAME을 수정하면 원하는 인코딩으로 가능
# 사용법

1. ~\ecconverter\ecconverter 경로에 targetfiles 폴더를 생성한 후, 변환을 원하는 xls(x) 혹은 csv파일을 넣는다.  
  (폴더 없이 최초 실행하면 폴더만 자동 생성된 후 종료되도록 설계되어 있음. 생성된 폴더에 변환 대상 파일을 집어 넣으면 됩니다.)


3. run


4. 콘솔에 뜨는 설명 참조하여 작업할 번호 입력



# 현재까지 발견된 edge case
## 공통

  
## excel->csv

1. excel자체 csv변환 기능을 사용하면 가장 긴 row수를 기준으로 나머지 행에도 뒤의 공백들을 구분하는 콤마(,)가 찍히지만 해당 프로젝트에서는 반영하지 않았음.


## csv->excel
1. excel의 자료형 구분 지원하지 않음.(제가 필요가 없었기 때문. 수정예정 없음.)
