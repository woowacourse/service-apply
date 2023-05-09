import "react-datepicker";

declare module "react-datepicker" {
  export interface ReactDatePickerProps {
    onChange?(
      date: WithRange extends false | undefined ? Date | null : [Date | null, Date | null],
      event: React.SyntheticEvent<any> | undefined
    ): void;
  }
}
