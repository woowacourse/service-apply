import useFormContext from "../../../hooks/useFormContext";
import Button from "../../@common/Button/Button";

const TempSaveButton = ({ onSaveTemp }) => {
  const { isValid } = useFormContext();

  return (
    <Button type="button" onClick={onSaveTemp} disabled={!isValid}>
      임시 저장
    </Button>
  );
};

export default TempSaveButton;
