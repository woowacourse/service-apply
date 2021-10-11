import Button from "../../@common/Button/Button";

const CancelButton = ({ onClick }) => {
  return (
    <Button type="button" variant="outlined" onClick={onClick}>
      취소
    </Button>
  );
};

export default CancelButton;
