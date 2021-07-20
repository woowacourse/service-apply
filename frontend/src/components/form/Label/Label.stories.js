import React from 'react';
import Label from './Label';

export default {
  title: 'form/Label',
  component: Label,
};

const Template = (args) => <Label {...args} />;

export const Default = Template.bind({});
Default.args = {
  children: '이름',
};

export const Required = Template.bind({});
Required.args = {
  children: '이름',
  required: true,
};
