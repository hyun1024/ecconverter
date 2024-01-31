# 유의사항

직접 사용할 목적으로 간단하게 만들어서 edge case가 다수 존재할 것으로 예상됩니다.

단순히 개인 작업의 편의를 위해 작성한 코드여서 모든 edge case를 수정하지는 않을 것 같습니다.

오프라인 작업을 고려하여 필요한 의존성을 .jar로 직접 주입했습니다.

직접 사용하는 환경에서는 파일이 클 경우 분리할 필요가 있어서 20만 row이상인 csv파일인 경우 10만row씩 개별 xlsx로 분할되어 저장되도록 설계했습니다.




# 사용법

1. 프로젝트 경로의 targetfiles 폴더 안에 변환을 원하는 xls(x) 혹은 csv파일을 넣는다.


2. run


3. 콘솔에 뜨는 설명 참조하여 작업할 번호 입력



# 현재까지 발견된 edge case
## 공통

 현재 코드상으로는 MS949로 변환. Config class의 ENCODING_NAME을 수정하면 원하는 인코딩으로 가능

  
## excel->csv

1. excel자체 csv변환 기능을 사용하면 가장 긴 row수를 기준으로 나머지 행에도 뒤의 공백들을 구분하는 콤마(,)가 찍히지만 해당 프로젝트에서는 반영하지 않았음.


## csv->excel
1. excel의 자료형 구분 지원하지 않음.(제가 필요가 없었기 때문. 수정할 지 모르겠음.)
