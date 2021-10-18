import React from 'react';
import PasswordEdit from './PasswordEdit';

export default {
  title: 'pages/PasswordEdit',
  component: PasswordEdit,
};

const Template = (args) => <PasswordEdit {...args} />;

export const Default = Template.bind({});

Default.args = {};
