aws dynamodb list-tables \
  --endpoint-url http://localhost:8000 \
  --query "TableNames[]" --output text \
| tr '\t' '\n' \
| while read -r table; do
    echo "Deleting $table"
    aws dynamodb delete-table \
      --endpoint-url http://localhost:8000 \
      --table-name "$table"
  done
