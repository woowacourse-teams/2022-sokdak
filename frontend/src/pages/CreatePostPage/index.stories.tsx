import { MemoryRouter as Router, Routes, Route } from 'react-router';

import CreatePostPage from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Pages/CreatePostPage',
  component: CreatePostPage,
} as ComponentMeta<typeof CreatePostPage>;

const Template = () => {
  return (
    <Router initialEntries={['/', { pathname: '/', state: { boardId: 1 } }]}>
      <Routes>
        <Route path="/" element={<CreatePostPage />} />
      </Routes>
    </Router>
  );
};

export const CreatePostPageTemplate: ComponentStory<typeof CreatePostPage> = Template.bind({});
CreatePostPageTemplate.args = {};
