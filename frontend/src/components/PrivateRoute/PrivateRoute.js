import { Redirect, Route } from "react-router-dom";
import { ERROR_MESSAGE } from "../../constants/messages";
import PATH from "../../constants/path";
import useTokenContext from "../../hooks/useTokenContext";

const PrivateRoute = ({ ...props }) => {
  const { token } = useTokenContext();

  if (!token) alert(ERROR_MESSAGE.ACCESS.REQUIRED_LOGIN);

  return token ? <Route {...props} /> : <Redirect to={PATH.LOGIN} />;
};

export default PrivateRoute;
