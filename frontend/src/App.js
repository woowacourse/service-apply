import React from "react";
import { BrowserRouter, Route, Switch } from "react-router-dom";
import "./api/api";
import MainFooter from "./components/MainFooter/MainFooter";
import MainHeader from "./components/MainHeader/MainHeader";
import PrivateRoute from "./components/PrivateRoute/PrivateRoute";
import ApplicantRegister from "./pages/ApplicantRegister/ApplicantRegister";
import ApplicationRegister from "./pages/ApplicationRegister/ApplicationRegister";
import Login from "./pages/Login/Login";
import PasswordEdit from "./pages/PasswordEdit/PasswordEdit";
import PasswordFind from "./pages/PasswordFind/PasswordFind";
import PasswordFindResult from "./pages/PasswordFindResult/PasswordFindResult";
import Recruits from "./pages/Recruits/Recruits";
import RecruitmentProvider from "./provider/RecruitmentProvider";
import TokenProvider from "./provider/TokenProvider";
import ScrollToTop from "./components/ScrollToTop/ScrollToTop";
import "./App.css";

const App = () => {
  return (
    <TokenProvider>
      <RecruitmentProvider>
        <BrowserRouter>
          <ScrollToTop />
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
      </RecruitmentProvider>
    </TokenProvider>
  );
};

export default App;
