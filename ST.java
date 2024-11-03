public class ST<Key extends Comparable<Key>, Value> {
    private Node root;
    private final boolean RED = true;
    private final boolean BLACK = false; 

    public ST() {} 
    
    public Value get(Key key) {
        return get(root, key);
    }
    
    private Value get(Node x, Key key) {
        if (x == null) {
            return null;
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            return get(x.left, key);
        }
        if (cmp > 0) {
            return get(x.right, key); 
        }
        return x.val;
    }
    
    public void put(Key key, Value val) {
		root = put(root, key, val);
		root.color = BLACK;
	}

	private Node put(Node x, Key key, Value val) {
		if (x == null) 
			return new Node(key, val);
		int cmp = key.compareTo(x.key);
		if (cmp < 0) 
			x.left = put(x.left, key, val);
		else if (cmp > 0) 
			x.right = put(x.right, key, val);
		else 
			x.val = val;
		if (isRed(x.right) && !isRed(x.left)) // this red bit is the fixUp() method
			x = rotateLeft(x);
		if (isRed(x.left) && isRed(x.left.left))
			x = rotateRight(x);
		if (isRed(x.left) && isRed(x.right))
			colorFlip(x);
		x.size = size(x.left) + size(x.right) + 1;
		return x;
	}


    public void delete(Key key) {
        root = delete(root, key);
    }

    private Node delete(Node x, Key key) {
		if (x == null) 
			return null;
        if (key.compareTo(x.key) < 0) {
			if (!isRed(x.left) && !isRed(x.left.left)) {
				x = moveRedLeft(x);
			}
			x.left = delete(x.left, key);
		}
		else { 
			if (isRed(x.left)) 
				rotateRight(x); 
			if (key.compareTo(x.key) == 0 && x.right == null)  
				return null;
			if (!isRed(x.right) && !isRed(x.right.left))
				x = moveRedRight(x); 
			if (key.compareTo(x.key) == 0) {
				x.val = get(x.right, min(x.right).key);
				x.key = min(x.right).key;
				x.right = deleteMin(x.right);
			}
			else {
				x.right = delete(x.right, key);
			}
		}
		x.size = size(x.left) + size(x.right) + 1;
		return fixUp(x);
	}			

    public boolean contains(Key key) {
        return contains(root, key);
    }
    
    private boolean contains(Node x, Key key) {
        if (x == null) {
            return false;
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            return contains(x.left, key);
        }
        if (cmp > 0) {
            return contains(x.right, key); 
        }
        return true;
    }

    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null) {
            return 0;
        }
        return x.size;
    }
    
    public boolean isEmpty() {
        return root == null;
    }
    
    public Key min() {
        return min(root).key;
    }
    
    private Node min(Node x) {
        if (x.left == null) {
            return x;
        }
        return min(x.left);
    }

    public Key max() {
        return max(root).key;
    }

    private Node max(Node x) {
        if (x.right == null) {
            return x;
        }
        return max(x.right);
    }
    
    public Key ceiling(Key key) {
        Node x = ceiling(root, key);
        if (x==null) {
            return null;
        } 
        return x.key;
    }

    private Node ceiling(Node x, Key key) {
        if (x == null) {
            return null;
        }
        int cmp = key.compareTo(x.key);
        if (cmp > 0) {
            return ceiling(x.right, key);
        }
        if (cmp < 0) {
            Node t = ceiling(x.left, key);
            if (t != null) {
                return t;
            }
        }
        return x;
    }

    public Key floor(Key key) {
        Node x = floor(root, key);
        if (x == null) {
            return null;
        }
        return x.key;
    }

    private Node floor(Node x, Key key) {
        if (x == null) {
            return null;
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            return floor(x.left, key);
        }
        if (cmp > 0) {
            Node t = floor(x.right, key);
            if (t != null) {
                return t;
            }
        }
        return x;
    }

    public void deleteMin() {
		root = deleteMin(root);
		root.color = BLACK;
	}

    private Node deleteMin(Node x) {
		if (x.left == null) 
			return null;
		if (!isRed(x.left) && !isRed(x.left.left)) {
			x = moveRedLeft(x);
        }
		x.left = deleteMin(x.left);
		return fixUp(x);
    }

    private Node moveRedLeft(Node x) {
        colorFlip(x);
        if (isRed(x.right.left)) {
            x.right = rotateRight(x.right); // words in blue reversed for right; delete this line
            x = rotateLeft(x);
            colorFlip(x);
        }
        return x;
    }
    
    private Node moveRedRight(Node x) {
        colorFlip(x);
        if (isRed(x.left.left)) {
            x = rotateRight(x);
            colorFlip(x);
        }
        return x;
    }
    
    private Node rotateLeft(Node x) {
        Node t = x.right;
        x.right = t.left;
        t.left = x;
        t.color = x.color;
        x.color = RED;
        t.size = x.size;
        x.size = 1 + size(x.left) + size(x.right);
        return t;
    }

    private Node rotateRight(Node x) {
        Node t = x.left;
        x.left = t.right;
        t.right = x;
        t.color = x.color;
        x.color = RED;
        t.size = x.size;
        x.size = 1 + size(x.left) + size(x.right);
        return t;
    }

    private boolean isRed(Node x) {
        if (x == null) {
            return false; // null links are black
        }
        return x.color;
    }

    private void colorFlip(Node x) {
        x.color = RED;
        x.left.color = BLACK;
        x.right.color = BLACK;
    }

    private Node fixUp(Node x) {
        if (isRed(x.right) && !isRed(x.left)) {
            x = rotateLeft(x);
        }
        if (isRed(x.left) && isRed(x.left.left)) {
            x = rotateRight(x);
        }
        if (isRed(x.left) && isRed(x.right)) {
            colorFlip(x);
        }
        return x;
    }

    private class Node {
        private Key key;
        private Value val;
        private int size = 1;
        private boolean color; // color of parent link
        private Node left, right;
        
        private Node(Key k, Value v) {
            key = k;
            val = v;
            color = RED;
        }
    }
}