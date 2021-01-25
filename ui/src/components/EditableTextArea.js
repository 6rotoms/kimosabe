import React, { useState } from 'react';
import PropTypes from 'prop-types';
import Button from './Button';
import Flex from './Flex';

const EditableTextArea = ({ initialText = '', onSave, isToggleable = false, charLimit = -1 }) => {
  const [bio, setBio] = useState(initialText);
  const [unsavedBio, setUnsavedBio] = useState(bio);
  const [editable, setEditable] = useState(false);

  const handleSave = () => {
    const shouldSave = editable;
    setEditable(!editable);
    setBio(unsavedBio);
    if (shouldSave) {
      onSave(unsavedBio);
    }
  };

  const handleChange = (text) => {
    let newText = text;
    if (charLimit > 0) {
      newText = newText.substring(0, Math.min(text.length, charLimit));
    }
    setUnsavedBio(newText);
  };

  const handleCancel = () => {
    setEditable(!editable);
    setUnsavedBio(bio);
  };

  return (
    <Flex wrap="wrap" direction="col" minHeight="full" className="w-full">
      {editable ? (
        <textarea
          data-testid="display-textarea"
          className={
            'flex-1 ' +
            'bg-grey-darkest ' +
            'px-px5 ' +
            'py-px10 ' +
            'h-inherit ' +
            'border-none ' +
            'rounded-md ' +
            'resize-none' +
            'break-words' +
            'whitespace-pre-wrap'
          }
          onChange={(e) => handleChange(e.target.value)}
          value={unsavedBio}
        />
      ) : (
        <Flex data-testid="display-text" className="flex-1 px-px5 py-px10">
          {bio}
        </Flex>
      )}
      {isToggleable && (
        <Flex justify="end" height="auto" data-testid="button-container">
          {editable && (
            <Button onClick={handleCancel} className="m-2.5 mr-0">
              Cancel
            </Button>
          )}
          <Button color="green" onClick={handleSave} className="m-2.5 mr-0">
            {editable ? 'Save' : 'Edit'}
          </Button>
        </Flex>
      )}
    </Flex>
  );
};

EditableTextArea.propTypes = {
  initialText: PropTypes.string,
  onSave: PropTypes.func.isRequired,
  isToggleable: PropTypes.bool,
  charLimit: PropTypes.number,
};

export default EditableTextArea;
