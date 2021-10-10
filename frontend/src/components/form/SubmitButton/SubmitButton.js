import useFormContext from "../../../hooks/useFormContext";
import Button from "../../@common/Button/Button";

const SubmitButton = ({ children }) => {
  const { isValid, isEmpty } = useFormContext();

  return <Button disabled={!isValid || isEmpty}>{children ?? "제출"}</Button>;
};

export default SubmitButton;
