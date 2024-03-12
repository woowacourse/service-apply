import React from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import "./api/api";
import "./App.css";
import Footer from "./components/Footer/Footer";
import Header from "./components/Header/Header";
import InquiryFloatingButton from "./components/InquiryFloatingButton/InquiryFloatingButton";
import PrivateRoute from "./components/PrivateRoute/PrivateRoute";
import ScrollToTop from "./components/ScrollToTop/ScrollToTop";
import { PATH } from "./constants/path";
import ApplicationRegister from "./pages/ApplicationRegister/ApplicationRegister";
import AssignmentSubmit from "./pages/AssignmentSubmit/AssignmentSubmit";
import Login from "./pages/Login/Login";
import MyApplication from "./pages/MyApplication/MyApplication";
import MyPage from "./pages/MyPage/MyPage";
import MyPageEdit from "./pages/MyPageEdit/MyPageEdit";
import PasswordEdit from "./pages/PasswordEdit/PasswordEdit";
import PasswordFind from "./pages/PasswordFind/PasswordFind";
import PasswordFindResult from "./pages/PasswordFindResult/PasswordFindResult";
import Recruits from "./pages/Recruits/Recruits";
import SignUp from "./pages/SignUp/SignUp";
import RecruitmentProvider from "./provider/RecruitmentProvider";
import TokenProvider from "./provider/TokenProvider";
import MemberInfoProvider from "./provider/MemberInfoProvider";
import { ModalProvider } from "./hooks/useModalContext";

const App = () => {
  return (
    <TokenProvider>
      <RecruitmentProvider>
        <ModalProvider>
          <BrowserRouter>
            <Header />
            <main className="main">
              <ScrollToTop>
                <Routes>
                  <Route path={PATH.HOME} element={<Recruits />} />
                  <Route path={PATH.RECRUITS} element={<Recruits />} />
                  <Route path={PATH.SIGN_UP} element={<SignUp />} />
                  <Route path={PATH.LOGIN} element={<Login />} />
                  <Route path={PATH.FIND_PASSWORD} element={<PasswordFind />} />
                  <Route path={PATH.FIND_PASSWORD_RESULT} element={<PasswordFindResult />} />
                  <Route element={<PrivateRoute />}>
                    <Route path={PATH.APPLICATION_FORM} element={<ApplicationRegister />} />
                    <Route path={PATH.EDIT_PASSWORD} element={<PasswordEdit />} />
                    <Route path={PATH.MY_APPLICATION} element={<MyApplication />} />
                    <Route path={PATH.ASSIGNMENT} element={<AssignmentSubmit />} />
                  </Route>

                  <Route element={<PrivateRoute />}>
                    <Route
                      path={PATH.MY_PAGE}
                      element={
                        <MemberInfoProvider>
                          <MyPage />
                        </MemberInfoProvider>
                      }
                    />
                    <Route
                      path={PATH.EDIT_MY_PAGE}
                      element={
                        <MemberInfoProvider>
                          <MyPageEdit />
                        </MemberInfoProvider>
                      }
                    />
                  </Route>
                </Routes>
              </ScrollToTop>
            </main>
            <Footer />
            <InquiryFloatingButton />
          </BrowserRouter>
        </ModalProvider>
      </RecruitmentProvider>
    </TokenProvider>
  );
};

export default App;
