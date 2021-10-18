import React from 'react';
import CheckBox from './CheckBox';

export default {
  title: 'form/CheckBox',
  component: CheckBox,
};

const Template = (args) => <CheckBox {...args} />;

export const Default = Template.bind({});
Default.args = {
  label: '체크박스',
};
