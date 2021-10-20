import { Redirect, Route } from "react-router-dom";
import PATH from "../../constants/path";
import useTokenContext from "../../hooks/useTokenContext";

const PrivateRoute = ({ ...props }) => {
  const { token } = useTokenContext();

  return token ? <Route {...props} /> : <Redirect to={PATH.LOGIN} />;
};

export default PrivateRoute;
