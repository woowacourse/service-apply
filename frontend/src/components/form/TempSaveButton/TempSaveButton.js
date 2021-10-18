import Button from "../../@common/Button/Button";

const TempSaveButton = ({ onSaveTemp, ...props }) => {
  return (
    <Button type="button" onClick={onSaveTemp} {...props}>
      임시 저장
    </Button>
  );
};

export default TempSaveButton;
