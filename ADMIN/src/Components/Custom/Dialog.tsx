import { Delete, Edit } from "@mui/icons-material"
import {
  Box,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  IconButton,
  Tooltip,
} from "@mui/material"
import { useEffect, useState } from "react";


interface CustomDialogProps {
  open: boolean;
}

const CustomDialog: React.FC<CustomDialogProps> = ({ open }) => {
  const [isOpen, setIsOpen] = useState(open);

  useEffect(()=>{
  },[open])

  // Function to close the dialog
  const handleClose = () => {
    setIsOpen(false);
  };

  return (
    <Dialog open={isOpen} onClose={handleClose}>
      <DialogTitle>Custom Dialog</DialogTitle>
      <DialogContent>
        <DialogContentText>
          This is a custom Material-UI dialog. You can customize the content here.
        </DialogContentText>
      </DialogContent>
      <DialogActions>
        <Button onClick={handleClose} color="primary">
          Close
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default CustomDialog;