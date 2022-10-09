import "react-datepicker";

/**
 * 선언병합은 왜 하였는가?
 * 1. react-datepicker는 BirthField 컴포넌트와 관련이 있다.
 * 2. BirthField의 props 중에서는 onChange가 존재한다.
 * 2. BirthField는 ReactDatePickerProps 타입을 상속받는다.
 * 3. ReactDatePickerProps 타입에서는 onChange가 optional이 아니다.
 * 4. BirthField의 onChange의 타입은 optional이어야 한다.
 * 5. 그렇기에 선언병합을 통해서 onChange 타입에 optional을 추가하였다.
 */
declare module "react-datepicker" {
  export interface ReactDatePickerProps {
    onChange?(
      date: WithRange extends false | undefined ? Date | null : [Date | null, Date | null],
      event: React.SyntheticEvent<any> | undefined
    ): void;
  }
}
