import useFormContext from "../../../hooks/useFormContext";
import Button from "../../@common/Button/Button";

const SubmitButton = () => {
  const { isValid, isEmpty } = useFormContext();

  return <Button disabled={!isValid || isEmpty}>제출</Button>;
};

export default SubmitButton;
