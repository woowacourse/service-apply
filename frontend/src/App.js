import React from "react";
import { BrowserRouter, Switch, Route } from "react-router-dom";
import "./api/api";
import PrivateRoute from "./components/PrivateRoute/PrivateRoute";
import MainHeader from "./components/MainHeader/MainHeader";
import MainFooter from "./components/MainFooter/MainFooter";
import Recruits from "./pages/Recruits/Recruits";
import ApplicantRegister from "./pages/ApplicantRegister/ApplicantRegister";
import ApplicationRegister from "./pages/ApplicationRegister/ApplicationRegister";
import Login from "./pages/Login/Login";
import PasswordFind from "./pages/PasswordFind/PasswordFind";
import PasswordEdit from "./pages/PasswordEdit/PasswordEdit";
import PasswordFindResult from "./pages/PasswordFindResult/PasswordFindResult";
import "./App.css";

const App = () => {
  return (
    <>
      <BrowserRouter>
        <MainHeader />
        <div className="main-view">
          <Switch>
            <Route path={["/", "/recruits"]} exact>
              <Recruits />
            </Route>
            <Route path="/applicants/new" exact>
              <ApplicantRegister />
            </Route>
            <Route path="/login" exact>
              <Login />
            </Route>
            <Route path="/find" exact>
              <PasswordFind />
            </Route>
            <Route path="/find/result" exact>
              <PasswordFindResult />
            </Route>
            <PrivateRoute path="/application-forms/:status" exact>
              <ApplicationRegister />
            </PrivateRoute>
            <Route path="/application-forms/:status" exact>
              <ApplicationRegister />
            </Route>
            <PrivateRoute path="/edit" exact>
              <PasswordEdit />
            </PrivateRoute>
          </Switch>
        </div>
        <MainFooter />
      </BrowserRouter>
    </>
  );
};

export default App;
