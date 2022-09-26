import { Outlet, Navigate } from "react-router-dom";
import { PATH } from "../../constants/path";
import useTokenContext from "../../hooks/useTokenContext";

const PrivateRoute = ({ ...props }) => {
  const { token } = useTokenContext();

  return token ? <Outlet {...props} /> : <Navigate to={PATH.LOGIN} />;
};

export default PrivateRoute;
