import { Button } from "../../components/form";
import useFormContext from "../../hooks/useFormContext";

const SubmitButton = () => {
  const { isValid, isEmpty } = useFormContext();

  return (
    <Button type="submit" disabled={!isValid || isEmpty}>
      제출
    </Button>
  );
};

export default SubmitButton;
