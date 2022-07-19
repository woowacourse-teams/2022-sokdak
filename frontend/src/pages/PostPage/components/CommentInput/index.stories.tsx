import { withRouter } from 'storybook-addon-react-router-v6';

import CommentInput from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Components/CommentInput',
  component: CommentInput,
  decorators: [withRouter],
} as ComponentMeta<typeof CommentInput>;

const Template = () => (
  <div style={{ width: '300px' }}>
    <CommentInput />
  </div>
);

export const CommentInputTemplate: ComponentStory<typeof CommentInput> = Template.bind({});
CommentInputTemplate.args = {};
