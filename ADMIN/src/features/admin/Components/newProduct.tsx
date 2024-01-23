import { ArrowBackIosNew, CloudUpload } from '@mui/icons-material';
import { Box, Button, Grid, IconButton, MenuItem, Paper, Select, Stack, TextField } from '@mui/material';
import { child, get, getDatabase, ref, set } from 'firebase/database';
import { useSnackbar } from 'notistack';
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import adminApi from '../../../apis/adminApi';
import { database } from '../../../firebase';

const NewProduct = () => {
    const [images, setImages] = useState<string[]>([]);
    const [idCategory, setIdCategory] = useState<string[]>([]);
    const [selectCategory, setSelectCategory] = useState<string>('');
    const imgRef = React.useRef<HTMLInputElement | null>(null);
    const [productData, setProductData] = useState({
        prod_name: '',
        price: '',
        prod_describe: '',
        // prod_quantity: '',
        prod_id : ''
    });
    const dbRef = ref(database);
    const db = getDatabase();
    const navigate = useNavigate();
    const { enqueueSnackbar } = useSnackbar();
    const handleDeleteImage = (value: string) => {
        setImages((prev) => prev.filter((item) => item !== value));
    };
    const handleFiles = async (e: React.ChangeEvent<HTMLInputElement>) => {
        const inputElement = e.target;
        if (inputElement.files) {
            const images = new FormData();
            const selectedFiles = inputElement.files;
            for (let i = 0; i < selectedFiles.length; i++) {
                const file = selectedFiles[i];
                images.append('file', file);
                images.append('upload_preset', 'oksl1k1o');
                try {
                    const response = await adminApi.getUploadImages(images);
                    if (response.status === 200) {
                        setImages((prev) => [...prev, response.data.secure_url]);
                    }
                } catch (err) {
                    console.log(err);
                }
            }
        }
    };

    useEffect(() => {
        get(child(dbRef, 'category'))
            .then((snapshot) => {
                if (snapshot.exists()) {
                    setIdCategory(Object.keys(snapshot.val()).map((key) => snapshot.val()[key].cat_id));
                } else {
                    console.log('loi');
                }
            })
            .catch((err) => {
                console.log(err);
            });
    }, []);

    const handleProductCreate = async () => {
        try {
            
            const data = {
                ...productData,
                prod_image : images[0],
                cat_id : selectCategory
            }
            set(ref(db, 'product/' + productData.prod_id), data);
            enqueueSnackbar('Tạo sản phẩm thành công !', { variant: 'success' });
            navigate('/admin/products')
        } catch (error) {
            enqueueSnackbar('Tạo sản phẩm không thành công !', { variant: 'error' });
        }
    };

    const handleSelectChange = (value: string) => {
        setSelectCategory(value);
    };

    return (
        <Box sx={{ height: '100%' }}>
            <Box>
                <Stack
                    direction="row"
                    alignItems="center"
                    sx={{
                        p: '10px',
                        boxShadow: '0 4px 2px -2px rgba(0, 0, 0, 0.2)',
                        position: 'relative',
                        zIndex: 10
                    }}
                >
                    <Button
                        size="small"
                        startIcon={<ArrowBackIosNew fontSize="small" />}
                        onClick={() => {
                            navigate('/admin/products');
                        }}
                        variant="contained"
                        sx={{ mr: '10px', textTransform: 'revert' }}
                    >
                        Sản phẩm
                    </Button>
                    <IconButton onClick={() => handleProductCreate()} size="small" sx={{ mr: '5px' }}>
                        <CloudUpload fontSize="small" />
                    </IconButton>
                    
                </Stack>
                <Box className="mx-5 mt-5 h-full">
                    <Grid container spacing={2} className="h-full">
                        <Grid item xs={6}>
                            <Grid container spacing={2}>
                                <Grid item xs={12}>
                                    <TextField
                                        label="Tên sản phẩm"
                                        variant="outlined"
                                        fullWidth
                                        value={productData.prod_name}
                                        onChange={(e) => setProductData({ ...productData, prod_name: e.target.value })}
                                    />
                                </Grid>
                                <Grid item xs={12}>
                                    <TextField
                                        label="Mã sản phẩm (prod_id)"
                                        variant="outlined"
                                        fullWidth
                                        value={productData.prod_id}
                                        onChange={(e) => setProductData({ ...productData, prod_id: e.target.value })}
                                    />
                                </Grid>
                                <Grid item xs={12}>
                                    <TextField
                                        label="Price"
                                        variant="outlined"
                                        fullWidth
                                        value={productData.price}
                                        onChange={(e) => setProductData({ ...productData, price: e.target.value })}
                                    />
                                </Grid>
                                <Grid item xs={12}>
                                    <TextField
                                        label="Description"
                                        variant="outlined"
                                        fullWidth
                                        value={productData.prod_describe}
                                        onChange={(e) => setProductData({ ...productData, prod_describe: e.target.value })}
                                    />
                                </Grid>
                                {/* <Grid item xs={12}>
                                    <TextField
                                        label="Số Lượng"
                                        variant="outlined"
                                        fullWidth
                                        value={productData.prod_quantity}
                                        onChange={(e) => setProductData({ ...productData, prod_quantity: e.target.value })}
                                    />
                                </Grid> */}
                                <Grid item xs={12}>
                                    <Select
                                        className="w-full"
                                        labelId="status-label"
                                        id="status-select"
                                        value={selectCategory || idCategory[0]}
                                        onChange={(e) => handleSelectChange(e.target.value)}
                                    >
                                        {idCategory.map((item) => (
                                            <MenuItem key={item} value={item}>
                                                {item}
                                            </MenuItem>
                                        ))}
                                    </Select>
                                </Grid>
                            </Grid>
                        </Grid>
                        <Grid item xs={6}>
                            <Paper elevation={3} sx={{ padding: '20px' }}>
                                <IconButton onClick={() => imgRef.current?.click()} size="small" sx={{ mb: '10px' }}>
                                    <CloudUpload fontSize="small" />
                                </IconButton>
                                <input onChange={(e) => handleFiles(e)} ref={imgRef} hidden type="file" id="file"  />
                                <div className="flex gap-4 items-center ">
                                    {images.map((item) => (
                                        <div className="relative w-1/4 h-1/4" key={item}>
                                            <img src={item} alt="preview" className=" h-full w-full object-cover rounded-md cursor-pointer" />
                                            <span
                                                title="xóa ảnh"
                                                onClick={() => handleDeleteImage(item)}
                                                className="absolute hover:bg-gray-400 top-[-20px] right-[-15px] w-10 h-10 flex items-center justify-center cursor-pointer p-2 text-white text-xl bg-gray-300 rounded-full"
                                            >
                                                xóa
                                            </span>
                                        </div>
                                    ))}
                                </div>
                            </Paper>
                        </Grid>
                    </Grid>
                </Box>
            </Box>
        </Box>
    );
};

export default NewProduct;
