import { useState } from "react";
import { ComponentMeta, ComponentStory } from "@storybook/react";
import MessageTextInput, { MessageTextInputProps } from "./MessageTextInput";

export default {
  title: "form/MessageTextInput",
  component: MessageTextInput,
} as ComponentMeta<typeof MessageTextInput>;

const Template: ComponentStory<typeof MessageTextInput> = (
  args: Omit<MessageTextInputProps, "onChange">
) => {
  const [value, setValue] = useState("");

  const handleChange: React.ChangeEventHandler<HTMLInputElement> = (e) => {
    return setValue(e.target.value);
  };

  return <MessageTextInput value={value} onChange={handleChange} {...args} />;
};

export const Default = Template.bind({});
Default.args = {
  label: "이름",
};

export const Required = Template.bind({});
Required.args = {
  label: "이름",
  required: true,
};

export const WithDescription = Template.bind({});
WithDescription.args = {
  label: "이름",
  description: (
    <div>
      자신을 드러낼 수 있는 개인 블로그, GitHub, 포트폴리오 주소 등이 있다면 입력해 주세요.
      <div>여러 개가 있는 경우 Notion, Google 문서 등을 사용하여 하나로 묶어 주세요.</div>
    </div>
  ),
};

export const MaxLength = Template.bind({});
MaxLength.args = {
  label: "이름",
  maxLength: 30,
  value: "썬",
};
