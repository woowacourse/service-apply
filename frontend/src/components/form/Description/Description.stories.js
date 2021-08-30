import React from "react";
import Description from "./Description";

export default {
  title: "form/Description",
  component: Description,
};

const Template = (args) => <Description {...args} />;

export const Default = Template.bind({});
Default.args = {
  children: "설명입니다!",
};
