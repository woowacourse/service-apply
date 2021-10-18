import React from "react";
import MyPageEdit from "./MyPageEdit";

export default {
  title: "pages/MyPageEdit",
  component: MyPageEdit,
};

const Template = (args) => <MyPageEdit {...args} />;

export const Default = Template.bind({});

Default.args = {};
