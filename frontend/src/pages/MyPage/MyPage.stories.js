import React from "react";
import MyPage from "./MyPage";

export default {
  title: "pages/MyPage",
  component: MyPage,
};

const Template = (args) => <MyPage {...args} />;

export const Default = Template.bind({});

Default.args = {};
