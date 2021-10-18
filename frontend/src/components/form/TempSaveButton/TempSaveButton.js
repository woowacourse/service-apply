import Button from '../../@common/Button/Button';

const TempSaveButton = ({ onSaveTemp }) => {
  return (
    <Button type="button" onClick={onSaveTemp}>
      임시 저장
    </Button>
  );
};

export default TempSaveButton;
