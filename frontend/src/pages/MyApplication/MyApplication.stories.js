import React from "react";

import MyApplication from "./MyApplication";

export default {
  title: "pages/MyApplication",
  component: MyApplication,
};

const Template = (args) => <MyApplication {...args} />;

export const Default = Template.bind({});
