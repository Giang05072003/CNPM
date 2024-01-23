import { Delete } from '@mui/icons-material';
import { Box, Button, IconButton, Stack, Typography } from '@mui/material';
import { child, get, getDatabase, ref, remove } from 'firebase/database';
import { MaterialReactTable, type MRT_ColumnDef, type MRT_ColumnFiltersState, type MRT_PaginationState, type MRT_SortingState } from 'material-react-table';
import { useSnackbar } from 'notistack';
import { useEffect, useMemo, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { database } from '../../firebase';
import { Product } from '../../models';
import { formatCurrencyVND } from '../../utils';
import SettingMenu from './Components/SettingMenu';

export const Products = () => {
    const dbRef = ref(database);
    const db = getDatabase();
    const navigate = useNavigate();
    const [data, setData] = useState<Product[]>([]);
    const [isLoading, setIsLoading] = useState(false);
    const [isError, setIsError] = useState(false);
    const [isRefetching, setIsRefetching] = useState(false);
    const { enqueueSnackbar } = useSnackbar();
    const [columnFilters, setColumnFilters] = useState<MRT_ColumnFiltersState>([]);
    const [globalFilter, setGlobalFilter] = useState('');
    const [sorting, setSorting] = useState<MRT_SortingState>([]);
    const [pagination, setPagination] = useState<MRT_PaginationState>({
        pageIndex: 0,
        pageSize: 10
    });
    const [isDel, setIsDel] = useState(false);
    const [open, setOpen] = useState(false);
    const settingRef = useRef<HTMLButtonElement>(null);
   
    const handleSelectRows = (row: any) => {
        const idData = row.map((item: any) => item.original.prod_id);
        (async () => {
            try {
                idData.forEach((productId: string) => {
                    const productRef = ref(db, `product/${productId}`);
                    remove(productRef)
                        .then(() => {
                            console.log(`Đã xóa sản phẩm có ID ${productId}`);
                        })
                        .catch((error) => {
                            console.error(`Lỗi khi xóa sản phẩm có ID ${productId}:`, error);
                        });
                });
                enqueueSnackbar('Xóa thành công', { variant: 'success' });
                setIsDel((item) => !item);
            } catch (error) {
                enqueueSnackbar('Có lỗi xảy ra thử lại sau', { variant: 'error' });
                console.log(error);
            }
        })();
    };
    useEffect(() => {
        const fetchData = async () => {
            if (!data.length) {
                setIsLoading(true);
            } else {
                setIsRefetching(true);
            }

            try {
                get(child(dbRef, 'product'))
                    .then((snapshot) => {
                        if (snapshot.exists()) {
                            setData(Object.keys(snapshot.val()).map((key) => snapshot.val()[key]));
                        } else {
                            console.log('loi');
                        }
                    })
                    .catch((err) => {
                        console.log(err);
                    });
            } catch (error) {
                setIsError(true);
                console.error(error);
                return;
            }

            setIsError(false);
            setIsLoading(false);
            setIsRefetching(false);
        };
        fetchData();
    }, [columnFilters, globalFilter, isDel, pagination.pageIndex, pagination.pageSize, sorting]);
    const columns = useMemo<MRT_ColumnDef<Product>[]>(
        () => [
            { accessorKey: 'prod_name', header: 'Tên sản phẩm' },
            { accessorKey: 'price', header: 'Giá sản phẩm', Cell: ({ cell }) => formatCurrencyVND(cell.getValue<string>()) },
            { accessorKey: 'prod_describe', header: 'Miêu tả sản phẩm' }
        ],
        []
    );

    return (
        <Box sx={{ height: '100%' }}>
            <SettingMenu anchorRef={settingRef} open={open} setOpen={setOpen} />
            <MaterialReactTable
                muiTablePaperProps={{ sx: { height: '100%' } }}
                muiTableContainerProps={{ sx: { height: 'calc(100%)' } }}
                columns={columns}
                data={data}
                enableRowSelection
                manualFiltering
                manualPagination
                muiTableBodyRowProps={({ row }) => ({
                    onClick: () => {
                        navigate(`/admin/update/product/${row.original.prod_id}`);
                    },
                    sx: { cursor: 'pointer' }
                })}
                manualSorting
                muiToolbarAlertBannerProps={
                    isError
                        ? {
                              color: 'error',
                              children: 'Error loading data'
                          }
                        : undefined
                }
                positionToolbarAlertBanner="bottom"
                muiLinearProgressProps={({ isTopToolbar }) => ({
                    sx: {
                        display: isTopToolbar ? 'block' : 'none' //hide bottom progress bar
                    }
                })}
                renderTopToolbarCustomActions={({ table }) => (
                    <Stack direction="row" alignItems="center">
                        <Button
                            disabled={isLoading}
                            sx={{ mr: '10px' }}
                            variant="contained"
                            onClick={() => {
                                navigate('/admin/new/product');
                            }}
                        >
                            Tạo
                        </Button>
                        <Typography sx={{ fontSize: '18px', fontWeight: 500, mr: '10px' }}>Sản phẩm</Typography>
                        {table.getSelectedRowModel().rows.length > 0 && (
                            <IconButton size="small" sx={{ mr: '5px' }} onClick={() => handleSelectRows(table.getSelectedRowModel().rows)}>
                                <Delete fontSize="small" htmlColor="black" />
                            </IconButton>
                        )}
                    </Stack>
                )}
                onColumnFiltersChange={setColumnFilters}
                onGlobalFilterChange={setGlobalFilter}
                onPaginationChange={setPagination}
                onSortingChange={setSorting}
                enableStickyHeader
                state={{
                    columnFilters,
                    globalFilter,
                    isLoading,
                    pagination,
                    showAlertBanner: isError,
                    showProgressBars: isRefetching,
                    sorting
                }}
            />
        </Box>
    );
};
