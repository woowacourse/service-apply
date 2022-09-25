import React, { useState } from "react";
import { ComponentMeta, ComponentStory } from "@storybook/react";
import Textarea, { TextareaProps } from "./Textarea";

export default {
  title: "form/Textarea",
  component: Textarea,
} as ComponentMeta<typeof Textarea>;

const Template: ComponentStory<typeof Textarea> = (args: Omit<TextareaProps, "onChange">) => {
  const [value, setValue] = useState("");

  const handleChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    return setValue(e.target.value);
  };

  return <Textarea value={value} onChange={handleChange} {...args} />;
};

export const Default = Template.bind({});
Default.args = {
  maxLength: 100,
};

export const ReadOnly = Template.bind({});

ReadOnly.args = {
  maxLength: 100,
  value: "읽기전용 입력창입니다.",
  readOnly: true,
};
