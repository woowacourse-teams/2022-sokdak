import PostListItem from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Components/PostListItem',
  component: PostListItem,
} as ComponentMeta<typeof PostListItem>;

const Template = (args: any) => <PostListItem {...args} />;

export const PostListItemTemplate: ComponentStory<typeof PostListItem> = Template.bind({});
PostListItemTemplate.args = {};
