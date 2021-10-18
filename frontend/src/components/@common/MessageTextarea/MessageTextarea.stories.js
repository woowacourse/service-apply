import React from 'react';
import MessageTextarea from './MessageTextarea';

export default {
  title: 'form/MessageTextarea',
  component: MessageTextarea,
};

const Template = (args) => <MessageTextarea {...args} />;

export const Default = Template.bind({});
Default.args = {
  label: '이름',
};

export const Required = Template.bind({});
Required.args = {
  label: '이름',
  required: true,
};

export const WithDescription = Template.bind({});
WithDescription.args = {
  label: '이름',
  description: '이름을 입력하세요.',
};

export const MaxLength = Template.bind({});
MaxLength.args = {
  label: '이름',
  maxLength: 30,
  value: '썬',
};
